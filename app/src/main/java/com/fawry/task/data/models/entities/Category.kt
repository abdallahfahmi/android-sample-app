package com.fawry.task.data.models.entities

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    val categoryId: Int,
    val name: String
)