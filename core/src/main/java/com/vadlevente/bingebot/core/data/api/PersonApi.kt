package com.vadlevente.bingebot.core.data.api

import com.vadlevente.bingebot.core.model.dto.CombinedCreditsResponseDto
import com.vadlevente.bingebot.core.model.dto.PersonDetailsDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PersonApi {

    @GET("person/{personId}/combined_credits")
    suspend fun fetchCombinedCredits(@Path("personId") personId: Int, @Query("language") language: String): CombinedCreditsResponseDto

    @GET("person/{personId}")
    suspend fun fetchDetails(@Path("personId") personId: Int, @Query("language") language: String): PersonDetailsDto
}