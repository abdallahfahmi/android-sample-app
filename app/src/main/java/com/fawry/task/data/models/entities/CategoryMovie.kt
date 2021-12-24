package com.fawry.task.data.models.entities

import androidx.room.Entity

@Entity(primaryKeys = ["categoryId", "movieId"])
data class CategoryMovie (
    val categoryId: Int,
    val movieId: Int
)