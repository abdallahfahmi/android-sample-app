package com.fawry.task.data.models

data class CategorizedMovies(
    val category: Category,
    val movies: List<Movie>
)