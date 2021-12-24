package com.fawry.task.data.repositories.movies_repository

import com.fawry.task.data.database.AppDatabase
import com.fawry.task.data.models.entities.CategorizedMovies
import com.fawry.task.data.models.entities.Category
import com.fawry.task.data.models.entities.CategoryMovie
import com.fawry.task.data.models.entities.Movie
import com.fawry.task.data.network.APIsService
import com.fawry.task.data.network.RemoteResult
import com.fawry.task.data.repositories.BaseRepository
import com.fawry.task.data.repositories.NetworkBoundResource
import kotlinx.coroutines.*
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val remoteDataSource: APIsService,
    private val localDataSource: AppDatabase
) : BaseRepository(), IMoviesRepository {

    override fun getMoviesCategorized(coroutineScope: CoroutineScope) =
        object : NetworkBoundResource<List<CategorizedMovies>>(coroutineScope) {

            //this approach is better than manually starting the worker and observing its state
            // to update the movies list because here we can propagate any failure to the user
            // and guarantee a response unlike worker that can be retried several times
            // before fetching the movies
            override suspend fun loadFromDb(): List<CategorizedMovies> {
                return queryCategorizedMoviesFromDB()
            }

            /**
             * this function to decide whether we need to fetch from remote api or not
             * in case cache got cleared (movies won't be fetched until next scheduled 4 hours)
             * */
            override fun shouldFetch(data: List<CategorizedMovies>): Boolean {
                /**fetch from remote api if database is empty,
                 * otherwise return the cached movies from database and don't proceed */
                return false
//                return data.isEmpty()
            }

            override suspend fun syncMoviesWithRemote() {
                syncMoviesFromRemoteServer(coroutineScope)
            }

        }.result

    override suspend fun syncMoviesFromRemoteServer(scope: CoroutineScope) {

        val categories = fetchRemoteCategories()
        clearDatabase()
        saveCategoriesToDB(categories)

        //fetch movies of each category concurrently and save them to database
        val concurrentTasks = arrayListOf<Deferred<Unit>>()
        categories.forEach {
            concurrentTasks.add(
                scope.async(Dispatchers.IO) {
                    val movies = fetchRemoteMoviesOfCategory(it.categoryId)
                    saveMoviesToDB(movies)
                }
            )
        }

        //wait for all movies to be fetched and written in database
        awaitAll(*concurrentTasks.toTypedArray())
    }

    private suspend fun queryCategorizedMoviesFromDB() =
        localDataSource.categoryMovieDao().getCategoryWithMovies()

    private suspend fun fetchRemoteCategories() = remoteDataSource.fetchCategories().genres

    private suspend fun fetchRemoteMoviesOfCategory(categoryId: Int) =
        remoteDataSource.fetchMovies(categoryId).results

    override suspend fun fetchMovieDetails(movieId: Int): RemoteResult<Movie> {
        return makeSafeApiCall {
            remoteDataSource.fetchMovieById(movieId)
        }
    }

    private suspend fun saveCategoriesToDB(categories: List<Category>) {
        localDataSource.categoriesDao().insert(*categories.toTypedArray())
    }

    private suspend fun saveMoviesToDB(movies: List<Movie>) {
        localDataSource.moviesDao().insert(*movies.toTypedArray())
        //add movieId with its corresponding categoryId in CategoryMovie table  (many to many relation)
        movies.forEach { movie ->
            movie.genre_ids.forEach { categoryId ->
                localDataSource.categoryMovieDao().insert(CategoryMovie(categoryId, movie.movieId))
            }
        }
    }

    private suspend fun clearDatabase() {
        localDataSource.categoriesDao().deleteAllCategories()
        localDataSource.moviesDao().deleteAllMovies()
        localDataSource.categoryMovieDao().deleteAllCategoryMovie()
    }

}