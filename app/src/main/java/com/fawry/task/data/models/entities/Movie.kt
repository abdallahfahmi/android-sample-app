package com.fawry.task.data.models.entities

import androidx.annotation.NonNull
import androidx.room.*
import com.fawry.task.data.database.Converter

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey
    @NonNull
    val id: Int,
    val title: String?,
    val poster_path: String?,
    val overview: String?,
    val vote_average: Double?,
    @TypeConverters(Converter::class)
    val genre_ids: List<Int> = listOf()
)