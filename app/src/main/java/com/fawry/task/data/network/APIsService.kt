package com.fawry.task.data.network

import com.fawry.task.data.models.CategoriesResponse
import com.fawry.task.data.models.Movie
import com.fawry.task.data.models.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIsService {

    @GET("genre/movie/list?api_key=c50f5aa4e7c95a2a553d29b81aad6dd0&language=en-US")
    suspend fun fetchCategories(): CategoriesResponse

    @GET("discover/movie?api_key=c50f5aa4e7c95a2a553d29b81aad6dd0&language=en-US")
    suspend fun fetchMovies(@Query("with_genres") genreId: Int): MoviesResponse

    @GET("movie/{movie_id}?api_key=c50f5aa4e7c95a2a553d29b81aad6dd0")
    suspend fun fetchMovieById(@Path("movie_id") id: Int): Movie
}