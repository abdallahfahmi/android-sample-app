package com.fawry.task.data.repositories.movies_repository

import com.fawry.task.data.database.AppDatabase
import com.fawry.task.data.models.Category
import com.fawry.task.data.models.GenreMovies
import com.fawry.task.data.models.Movie
import com.fawry.task.data.network.APIsService
import com.fawry.task.data.network.RemoteResult
import com.fawry.task.data.repositories.BaseRepository
import com.fawry.task.data.repositories.NetworkBoundResource
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val remoteDataSource: APIsService,
    private val localDataSource: AppDatabase
) : BaseRepository(), IMoviesRepository {

    private suspend fun getDatabaseCategories(): List<Category> {
        return localDataSource.categoriesDao().getCategories() ?: listOf()
    }

    private suspend fun getDatabaseMovies(): List<Movie> {
        return localDataSource.moviesDao().getMovies() ?: listOf()
    }

    private suspend fun fetchCategories(): RemoteResult<List<Category>> {
        return makeApiCall {
            remoteDataSource.fetchCategories().genres
        }
    }

    private suspend fun fetchMovies(categoryId: Int): RemoteResult<List<Movie>> {
        return makeApiCall {
            remoteDataSource.fetchMovies(categoryId).results
        }
    }

    override fun getMoviesCategorized(coroutineScope: CoroutineScope) =
        object : NetworkBoundResource<List<GenreMovies>>(coroutineScope) {

            override suspend fun loadFromDb(): List<GenreMovies>? {
                val categories = getDatabaseCategories()
                val movies = getDatabaseMovies()
                return if (categories.isNullOrEmpty())
                    null
                else groupCategoriesAndMovies(categories, movies)
            }

            /**
             * this function to decide whether we need to fetch from remote api or not
             * in case user opened the app for the first time (before first 4 hours sync)
             * or the cache got cleared
             * */
            override fun shouldFetch(data: List<GenreMovies>?): Boolean {
                /**fetch from remote api if database is empty,
                 * otherwise return the cached movies from database and don't proceed */
                return data == null
            }

            override suspend fun fetchRemoteCategories(): RemoteResult<List<Category>> {
                return fetchCategories()
            }

            override suspend fun fetchRemoteMovies(categoryId: Int): RemoteResult<List<Movie>> {
                return fetchMovies(categoryId)
            }

            override suspend fun saveCategoriesToDB(data: List<Category>) {
                localDataSource.categoriesDao().insert(*data.toTypedArray())
            }

            override suspend fun saveMoviesToDB(data: List<Movie>) {
                localDataSource.moviesDao().insert(*data.toTypedArray())
            }

        }.result

    override suspend fun fetchMovieDetails(movieId: Int) =
        makeApiCall {
            remoteDataSource.fetchMovieById(movieId)
        }

    private fun groupCategoriesAndMovies(
        categories: List<Category>,
        movies: List<Movie>
    ): List<GenreMovies> {
        val list = arrayListOf<GenreMovies>()
        categories.forEach { category ->
            list.add(GenreMovies(category, movies.filter { it.genre_ids.contains(category.id) }))
        }
        return list
    }

}