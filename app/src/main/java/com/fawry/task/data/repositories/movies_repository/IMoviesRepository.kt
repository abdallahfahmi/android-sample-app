package com.fawry.task.data.repositories.movies_repository

import com.fawry.task.data.models.Category
import com.fawry.task.data.models.Movie
import com.fawry.task.data.network.RemoteResult

interface IMoviesRepository {

    suspend fun fetchCategories(): RemoteResult<List<Category>>

    suspend fun fetchMovies(categoryId: Int): RemoteResult<List<Movie>>

    suspend fun fetchMovieDetails(movieId: Int): Unit
}