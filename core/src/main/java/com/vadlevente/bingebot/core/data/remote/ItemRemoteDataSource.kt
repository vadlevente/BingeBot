package com.vadlevente.bingebot.core.data.remote

import com.vadlevente.bingebot.core.data.api.ItemApi
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.model.ItemDetails
import com.vadlevente.bingebot.core.model.SelectedLanguage

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
    ).results as List<T>
    suspend fun getItemDetails(
        itemId: Int,
        language: SelectedLanguage,
    ) = itemApi.fetchItemDetails(
        itemId = itemId,
        language = language.code,
    ) as ItemDetails<T>

}