package com.vadlevente.bingebot.core.data.cache

import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.model.SelectedLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface ItemCacheDataSource <T : Item> {
    val itemsState: StateFlow<List<T>>
    fun updateItems(items: List<T>, language: SelectedLanguage)
}

class ItemCacheDataSourceImpl <T : Item> @Inject constructor(): ItemCacheDataSource<T> {

    private var _itemsState = MutableStateFlow(emptyList<T>())
    override val itemsState = _itemsState.asStateFlow()

    override fun updateItems(items: List<T>, language: SelectedLanguage) {
        _itemsState.update {
            items.mapNotNull { it.copyLocale(language.code) as? T }
        }
    }

}