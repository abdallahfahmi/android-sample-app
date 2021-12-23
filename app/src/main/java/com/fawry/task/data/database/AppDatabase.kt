package com.fawry.task.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fawry.task.data.database.daos.CategoryDao
import com.fawry.task.data.database.daos.MovieDao
import com.fawry.task.data.models.Category
import com.fawry.task.data.models.Movie

@Database(entities = [Movie::class, Category::class], version = 1)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun moviesDao(): MovieDao

    abstract fun categoriesDao(): CategoryDao

}