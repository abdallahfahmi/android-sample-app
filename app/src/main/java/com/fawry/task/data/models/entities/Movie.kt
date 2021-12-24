package com.fawry.task.data.models.entities

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movies")
data class Movie @JvmOverloads constructor(
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    val movieId: Int,
    val title: String?,
    val poster_path: String?,
    val overview: String?,
    val vote_average: Double?,
    @Ignore
    val genre_ids: List<Int> = listOf()
)