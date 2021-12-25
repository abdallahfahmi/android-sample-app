package com.fawry.task.data.repositories.movies_repository.data_source

import com.fawry.task.data.database.AppDatabase
import com.fawry.task.data.models.entities.Category
import com.fawry.task.data.models.entities.CategoryMovie
import com.fawry.task.data.models.entities.Movie
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val database: AppDatabase) {

    suspend fun queryCategorizedMovies() =
        database.categoryMovieDao().getCategoryWithMovies()

    suspend fun saveCategories(categories: List<Category>) {
        database.categoriesDao().insert(*categories.toTypedArray())
    }

    suspend fun saveMovies(movies: List<Movie>) {
        database.moviesDao().insert(*movies.toTypedArray())
        //add movieId with its corresponding categoryId in CategoryMovie table (many to many relation)
        movies.forEach { movie ->
            movie.genre_ids.forEach { categoryId ->
                database.categoryMovieDao().insert(CategoryMovie(categoryId, movie.movieId))
            }
        }
    }

    suspend fun clearDatabase() {
        database.categoriesDao().deleteAllCategories()
        database.moviesDao().deleteAllMovies()
        database.categoryMovieDao().deleteAllCategoryMovie()
    }

}