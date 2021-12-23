package com.fawry.task.data.repositories.movies_repository

import com.fawry.task.data.database.AppDatabase
import com.fawry.task.data.models.CategorizedMovies
import com.fawry.task.data.models.Category
import com.fawry.task.data.models.Movie
import com.fawry.task.data.network.APIsService
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

            override suspend fun loadFromDb(): List<CategorizedMovies>? {
                val categories = getCategoriesFromDB()
                val movies = getMoviesFromDB()
                return if (categories.isNullOrEmpty() || movies.isNullOrEmpty())
                    null
                else groupCategoriesAndMovies(categories, movies)
            }

            /**
             * this function to decide whether we need to fetch from remote api or not
             * in case user opened the app for the first time (before first 4 hours sync)
             * or the cache got cleared
             * */
            override fun shouldFetch(data: List<CategorizedMovies>?): Boolean {
                /**fetch from remote api if database is empty,
                 * otherwise return the cached movies from database and don't proceed */
                return data == null
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
            concurrentTasks.add(scope.async(Dispatchers.IO) { fetchRemoteMoviesAndSaveToDatabase(it.id) })
        }

        //wait for all movies to be fetched and written in database
        awaitAll(*concurrentTasks.toTypedArray())
    }

    override suspend fun fetchMovieDetails(movieId: Int) =
        makeSafeApiCall {
            remoteDataSource.fetchMovieById(movieId)
        }

    private suspend fun getCategoriesFromDB() = localDataSource.categoriesDao().getCategories()

    private suspend fun saveCategoriesToDB(categories: List<Category>) {
        localDataSource.categoriesDao().insert(*categories.toTypedArray())
    }

    private suspend fun getMoviesFromDB() = localDataSource.moviesDao().getMovies()

    private suspend fun saveMoviesToDB(movies: List<Movie>) {
        localDataSource.moviesDao().insert(*movies.toTypedArray())
    }

    private suspend fun fetchRemoteCategories() = remoteDataSource.fetchCategories().genres

    private suspend fun fetchRemoteMovies(categoryId: Int) =
        remoteDataSource.fetchMovies(categoryId).results

    private suspend fun fetchRemoteMoviesAndSaveToDatabase(categoryId: Int) {
        val movies = fetchRemoteMovies(categoryId)
        saveMoviesToDB(movies)
    }

    private fun groupCategoriesAndMovies(
        categories: List<Category>,
        movies: List<Movie>
    ): List<CategorizedMovies> {
        val list = arrayListOf<CategorizedMovies>()
        categories.forEach { category ->
            list.add(
                CategorizedMovies(
                    category,
                    movies.filter { it.genre_ids.contains(category.id) })
            )
        }
        return list
    }

}