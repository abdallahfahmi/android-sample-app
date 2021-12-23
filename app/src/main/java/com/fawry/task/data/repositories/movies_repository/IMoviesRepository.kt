package com.fawry.task.data.repositories.movies_repository

import androidx.lifecycle.LiveData
import com.fawry.task.data.models.Category
import com.fawry.task.data.models.GenreMovies
import com.fawry.task.data.models.Movie
import com.fawry.task.data.network.RemoteResult
import kotlinx.coroutines.CoroutineScope

interface IMoviesRepository {

    fun getMoviesCategorized(coroutineScope: CoroutineScope): LiveData<RemoteResult<List<GenreMovies>>>

    suspend fun fetchMovieDetails(movieId: Int): RemoteResult<Movie>

}