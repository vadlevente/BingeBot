package com.vadlevente.bingebot.watchlist.domain.usecase

import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.data.repository.ItemRepository
import com.vadlevente.bingebot.core.model.DisplayedItem
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.usecase.BaseUseCase
import com.vadlevente.bingebot.core.util.getThumbnailUrl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class GetWatchListItemsUseCaseParams(
    val watchListId: String,
    val query: String? = null,
)

class GetWatchListItemsUseCase <T : Item> @Inject constructor(
    private val itemRepository: ItemRepository<T>,
    private val preferencesDataSource: PreferencesDataSource,
) : BaseUseCase<GetWatchListItemsUseCaseParams, List<DisplayedItem<T>>> {

    override fun execute(params: GetWatchListItemsUseCaseParams): Flow<List<DisplayedItem<T>>> =
        combine(
            itemRepository.getWatchListItems(params.watchListId),
            preferencesDataSource.apiConfiguration,
            ::Pair,
        ).map { (items, configuration) ->
            items
                .filter { item ->
                    params.query?.let { query ->
                        if (query.isEmpty()) return@let false
                        item.title.lowercase().contains(query.lowercase()) ||
                            item.originalTitle.lowercase().contains(query.lowercase())
                    } ?: true
                }
                .map { item ->
                    DisplayedItem(
                        item = item,
                        thumbnailUrl = item.getThumbnailUrl(configuration),
                    )
                }
        }

}