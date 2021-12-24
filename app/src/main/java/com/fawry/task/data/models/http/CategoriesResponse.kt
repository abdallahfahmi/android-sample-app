package com.fawry.task.data.models.http

import com.fawry.task.data.models.entities.Category

data class CategoriesResponse(
    val genres: List<Category>
)