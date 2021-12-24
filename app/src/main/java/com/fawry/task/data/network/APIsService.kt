package com.fawry.task.data.network

import com.fawry.task.BuildConfig
import com.fawry.task.data.models.entities.Movie
import com.fawry.task.data.models.http.CategoriesResponse
import com.fawry.task.data.models.http.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIsService {

    @GET("genre/movie/list")
    suspend fun fetchCategories(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): CategoriesResponse

    @GET("discover/movie")
    suspend fun fetchMovies(
        @Query("with_genres") genreId: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): MoviesResponse

    @GET("movie/{movie_id}")
    suspend fun fetchMovieById(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Movie

}