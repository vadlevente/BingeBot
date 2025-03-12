package com.vadlevente.bingebot.core.data.api

import GenresResponseDto
import com.vadlevente.bingebot.core.model.Item.Tv
import com.vadlevente.bingebot.core.model.dto.CreditsDto
import com.vadlevente.bingebot.core.model.dto.ItemDto.TvDto
import com.vadlevente.bingebot.core.model.dto.ItemsResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvApi : ItemApi {

    @GET("search/tv")
    override suspend fun searchItems(@Query("query") query: String, @Query("language") language: String): ItemsResponseDto<Tv, TvDto>

    @GET("genre/tv/list")
    override suspend fun fetchGenres(@Query("language") language: String): GenresResponseDto

    @GET("tv/{itemId}")
    override suspend fun fetchItemDetails(@Path("itemId") itemId: Int, @Query("language") language: String): TvDto

    @GET("tv/{itemId}/credits")
    override suspend fun fetchItemCredits(@Path("itemId") itemId: Int, @Query("language") language: String): CreditsDto
}