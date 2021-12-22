package com.fawry.task.data.repositories.movies_repository

import com.fawry.task.data.database.AppDatabase
import com.fawry.task.data.models.Category
import com.fawry.task.data.models.Movie
import com.fawry.task.data.network.APIsService
import com.fawry.task.data.network.RemoteResult
import com.fawry.task.data.repositories.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoviesRepository @Inject constructor (
    private val remoteDataSource: APIsService,
    private val localDataSource: AppDatabase
): BaseRepository(), IMoviesRepository {

    override suspend fun fetchCategories(): RemoteResult<List<Category>> {
        return makeApiCall {
            remoteDataSource.fetchCategories().genres
        }.also {
//            GlobalScope.launch(Dispatchers.IO) {
//                localDataSource.categoriesDao().insert(*it.data!!.toTypedArray())
//            }
        }
    }

    override suspend fun fetchMovies(categoryId: Int): RemoteResult<List<Movie>> {
        return makeApiCall {
            remoteDataSource.fetchMovies(categoryId).results

        }.also {
//            GlobalScope.launch(Dispatchers.IO) {
//                localDataSource.moviesDao().insert(*it.data!!.toTypedArray())
//            }
        }
    }

    override suspend fun fetchMovieDetails(movieId: Int) {

    }

}