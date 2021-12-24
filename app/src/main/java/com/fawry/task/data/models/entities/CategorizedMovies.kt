package com.fawry.task.data.models.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CategorizedMovies(
    @Embedded
    val category: Category,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "movieId",
        associateBy = Junction(CategoryMovie::class)
    )
    val movies: List<Movie>
)