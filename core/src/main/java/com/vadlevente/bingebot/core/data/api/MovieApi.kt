package com.vadlevente.bingebot.core.data.api

import GenresResponseDto
import com.vadlevente.bingebot.core.model.ApiConfiguration
import com.vadlevente.bingebot.core.model.MovieDetails
import com.vadlevente.bingebot.core.model.dto.MoviesResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("search/movie")
    suspend fun searchMovies(@Query("query") query: String, @Query("language") language: String): MoviesResponseDto

    @GET("genre/movie/list")
    suspend fun fetchGenres(@Query("language") language: String): GenresResponseDto

    @GET("configuration")
    suspend fun fetchConfiguration(): ApiConfiguration

    @GET("movie/{movieId}")
    suspend fun fetchMovieDetails(@Path("movieId") movieId: Int, @Query("language") language: String): MovieDetails
}