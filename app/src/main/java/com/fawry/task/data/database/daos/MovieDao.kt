package com.fawry.task.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fawry.task.data.models.entities.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg movies: Movie)

    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()

}