package com.vadlevente.bingebot.core.data.api

import GenresResponseDto
import com.vadlevente.bingebot.core.model.ApiConfiguration
import com.vadlevente.bingebot.core.model.Movie
import retrofit2.http.GET

interface MovieApi {

    @GET("search/movie")
    suspend fun searchMovies(): List<Movie>

    @GET("genre/movie/list")
    suspend fun fetchGenres(): GenresResponseDto

    @GET("configuration")
    suspend fun fetchConfiguration(): ApiConfiguration
}