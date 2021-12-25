package com.fawry.task.data.repositories.movies_repository.data_source

import com.fawry.task.data.models.entities.Movie
import com.fawry.task.data.network.APIsService
import com.fawry.task.data.network.RemoteResult
import com.fawry.task.data.repositories.BaseDataSource
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apisService: APIsService
) : BaseDataSource() {

    suspend fun fetchMovieDetails(movieId: Int): RemoteResult<Movie> {
        return makeApiCall {
            apisService.fetchMovieById(movieId)
        }
    }

    suspend fun fetchRemoteCategories() =
        apisService.fetchCategories().genres

    suspend fun fetchRemoteMoviesOfCategory(categoryId: Int) =
        apisService.fetchMovies(categoryId).results

}