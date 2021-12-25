package com.fawry.task.data.repositories.movies_repository

import androidx.lifecycle.LiveData
import com.fawry.task.data.models.entities.CategorizedMovies
import com.fawry.task.data.models.entities.Movie
import com.fawry.task.data.network.RemoteResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope

interface IMoviesRepository {

    fun getMoviesCategorized(coroutineScope: CoroutineScope): LiveData<RemoteResult<List<CategorizedMovies>>>

    suspend fun fetchMovieDetails(movieId: Int): RemoteResult<Movie>

    suspend fun syncMoviesFromRemoteServer(scope: CoroutineScope)

}