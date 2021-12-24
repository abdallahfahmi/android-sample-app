package com.fawry.task.data.models.entities

data class CategorizedMovies(
    val category: Category,
    val movies: List<Movie>
)