package com.vadlevente.bingebot.core.data.remote

import com.vadlevente.bingebot.core.data.api.ItemApi
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.model.SelectedLanguage
import com.vadlevente.bingebot.core.model.dto.ItemDto

abstract class ItemRemoteDataSource <T : Item>(
    private val itemApi: ItemApi,
) {

    suspend fun getGenres(language: SelectedLanguage) = itemApi.fetchGenres(
        language = language.code,
    )

    suspend fun searchItem(
        query: String,
        language: SelectedLanguage,
    ) = itemApi.searchItems(
        query = query,
        language = language.code,
    ).results as List<ItemDto<T>>

    suspend fun getItemDetails(
        itemId: Int,
        language: SelectedLanguage,
    ) = itemApi.fetchItemDetails(
        itemId = itemId,
        language = language.code,
    ) as ItemDto<T>

    suspend fun getItemCredits(
        itemId: Int,
        language: SelectedLanguage,
    ) = itemApi.fetchItemCredits(
        itemId = itemId,
        language = language.code,
    )

}