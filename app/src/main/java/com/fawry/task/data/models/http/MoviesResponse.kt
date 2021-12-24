package com.fawry.task.data.models.http

import com.fawry.task.data.models.entities.Movie

data class MoviesResponse(
    val results: List<Movie>
)