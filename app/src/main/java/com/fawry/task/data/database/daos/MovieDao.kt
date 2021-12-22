package com.fawry.task.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fawry.task.data.models.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg movies: Movie)

    @Query("SELECT * FROM movies")
    fun getMovies(): List<Movie>?

    @Query("DELETE FROM movies")
    fun deleteAllMovies()

}