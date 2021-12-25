package com.fawry.task.data.repositories.movies_repository

import com.fawry.task.data.models.entities.CategorizedMovies
import com.fawry.task.data.models.entities.Movie
import com.fawry.task.data.network.RemoteResult
import com.fawry.task.data.repositories.NetworkBoundResource
import com.fawry.task.data.repositories.movies_repository.data_source.LocalDataSource
import com.fawry.task.data.repositories.movies_repository.data_source.RemoteDataSource
import kotlinx.coroutines.*
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : IMoviesRepository {

    override suspend fun syncMoviesFromRemoteServer(scope: CoroutineScope) {

        val categories = remoteDataSource.fetchRemoteCategories()
        localDataSource.clearDatabase()
        localDataSource.saveCategories(categories)

        //fetch movies of each category concurrently and save them to database
        val concurrentTasks = arrayListOf<Deferred<Unit>>()
        categories.forEach {
            concurrentTasks.add(
                scope.async(Dispatchers.IO) {
                    val movies = remoteDataSource.fetchRemoteMoviesOfCategory(it.categoryId)
                    localDataSource.saveMovies(movies)
                }
            )
        }

        //wait for all movies to be fetched and written in database
        awaitAll(*concurrentTasks.toTypedArray())
    }


    override fun getMoviesCategorized(coroutineScope: CoroutineScope) =
        object : NetworkBoundResource<List<CategorizedMovies>>(coroutineScope) {

            override suspend fun loadFromDb(): List<CategorizedMovies> {
                return localDataSource.queryCategorizedMovies()
            }

            /**
             * this function to decide whether we need to fetch from remote api or not
             * in case cache got cleared (movies won't be fetched until next scheduled 4 hours)
             * */
            override fun shouldFetch(data: List<CategorizedMovies>): Boolean {
                /**fetch from remote api if database is empty,
                 * otherwise return the cached movies from database and don't proceed */
                return data.isEmpty()
            }

            override suspend fun syncWithRemote() {
                syncMoviesFromRemoteServer(coroutineScope)
            }

        }.result


    override suspend fun fetchMovieDetails(movieId: Int): RemoteResult<Movie> {
        return remoteDataSource.fetchMovieDetails(movieId)
    }

}