package com.fawry.task.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fawry.task.data.models.Category

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg category: Category)

    @Query("SELECT * FROM categories")
    fun getCategories(): List<Category>?

    @Query("DELETE FROM categories")
    fun deleteAllCategories()

}