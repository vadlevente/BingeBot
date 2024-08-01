package com.vadlevente.bingebot.core.data.api

import GenresResponseDto
import com.vadlevente.bingebot.core.model.Item.Movie
import com.vadlevente.bingebot.core.model.ItemDetails.MovieDetails
import com.vadlevente.bingebot.core.model.dto.ItemsResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi : ItemApi {

    @GET("search/movie")
    override suspend fun searchItems(@Query("query") query: String, @Query("language") language: String): ItemsResponseDto<Movie>

    @GET("genre/movie/list")
    override suspend fun fetchGenres(@Query("language") language: String): GenresResponseDto

    @GET("movie/{itemId}")
    override suspend fun fetchItemDetails(@Path("itemId") itemId: Int, @Query("language") language: String): MovieDetails
}