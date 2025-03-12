package com.vadlevente.bingebot.core.data.api

import GenresResponseDto
import com.vadlevente.bingebot.core.model.dto.CreditsDto
import com.vadlevente.bingebot.core.model.dto.ItemDto
import com.vadlevente.bingebot.core.model.dto.ItemsResponseDto

interface ItemApi {

    suspend fun searchItems(
        query: String,
        language: String,
    ): ItemsResponseDto<*,*>

    suspend fun fetchGenres(
        language: String,
    ): GenresResponseDto

    suspend fun fetchItemDetails(
        itemId: Int,
        language: String,
    ): ItemDto<*>

    suspend fun fetchItemCredits(
        itemId: Int,
        language: String,
    ): CreditsDto
}