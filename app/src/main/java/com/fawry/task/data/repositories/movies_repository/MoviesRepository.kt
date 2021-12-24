package com.fawry.task.data.repositories.movies_repository

import android.util.Log
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

    suspend fun queryCategorizedMoviesFromDB() =
        localDataSource.categoryMovieDao().getCategoryWithMovies()

    override fun getMoviesCategorized(coroutineScope: CoroutineScope) =
        object : NetworkBoundResource<List<CategorizedMovies>>(coroutineScope) {

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
                return data.isEmpty()

                //check if worker status is running to avoid multiple sync
            }

            override suspend fun syncMoviesWithRemote() {
                syncMoviesFromRemoteServer(coroutineScope)
            }

        }.result

    override suspend fun syncMoviesFromRemoteServer(scope: CoroutineScope) {

        val categories = fetchRemoteCategories()
        saveCategoriesToDB(categories)

        //fetch movies of each category concurrently and save them to database
        val concurrentTasks = arrayListOf<Deferred<Unit>>()
        categories.forEach {
            concurrentTasks.add(scope.async(Dispatchers.IO) { fetchRemoteMoviesAndSaveToDatabase(it.categoryId) })
        }

        //wait for all movies to be fetched and written in database
        awaitAll(*concurrentTasks.toTypedArray())
    }

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
        //add foreign keys of movies and category (many to many relation)
        movies.forEach { movie ->
            movie.genre_ids.forEach { categoryId ->
                localDataSource.categoryMovieDao().insert(CategoryMovie(categoryId, movie.movieId))
            }
        }
    }

    private suspend fun fetchRemoteCategories() = remoteDataSource.fetchCategories().genres

    private suspend fun fetchRemoteMovies(categoryId: Int) =
        remoteDataSource.fetchMovies(categoryId).results

    private suspend fun fetchRemoteMoviesAndSaveToDatabase(categoryId: Int) {
        val movies = fetchRemoteMovies(categoryId)
        saveMoviesToDB(movies)
    }

}