package com.fawry.task.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fawry.task.data.database.daos.CategoryDao
import com.fawry.task.data.database.daos.CategoryMovieDao
import com.fawry.task.data.database.daos.MovieDao
import com.fawry.task.data.models.entities.Category
import com.fawry.task.data.models.entities.CategoryMovie
import com.fawry.task.data.models.entities.Movie

@Database(entities = [Movie::class, Category::class, CategoryMovie::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun moviesDao(): MovieDao

    abstract fun categoriesDao(): CategoryDao

    abstract fun categoryMovieDao(): CategoryMovieDao

}