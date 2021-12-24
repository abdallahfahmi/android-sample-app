package com.fawry.task.data.database.daos

import androidx.room.*
import com.fawry.task.data.models.entities.CategorizedMovies
import com.fawry.task.data.models.entities.CategoryMovie

@Dao
interface CategoryMovieDao {

    @Transaction
    @Query("SELECT * FROM categories")
    suspend fun getCategoryWithMovies(): List<CategorizedMovies>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg categoryMovie: CategoryMovie)

}