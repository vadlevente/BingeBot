package com.vadlevente.bingebot.list.domain.usecase

import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.data.repository.ItemRepository
import com.vadlevente.bingebot.core.model.DisplayedItem
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.usecase.BaseUseCase
import com.vadlevente.bingebot.core.util.getThumbnailUrl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNextItemsToWatchUseCase <T : Item> @Inject constructor(
    private val itemRepository: ItemRepository<T>,
    private val preferencesDataSource: PreferencesDataSource,
) : BaseUseCase<Unit, List<DisplayedItem<T>>> {
    override fun execute(params: Unit): Flow<List<DisplayedItem<T>>> =
        preferencesDataSource.displayNextToWatch.flatMapLatest { display ->
            if (!display) return@flatMapLatest flowOf(emptyList())
            combine(
                itemRepository.getItems(),
                preferencesDataSource.apiConfiguration,
                ::Pair,
            ).map { (items, configuration) ->
                val lastWatchedItems =
                    items.filter { it.isWatched }.sortedByDescending { it.watchedDate }
                        .take(COUNT_NEXT_ITEMS_TO_WATCH)
                val categoriesToFilter = lastWatchedItems.flatMap { it.genreCodes }.distinct()
                items.filter { !it.isWatched }
                    .filter { it.genreCodes.any { it in categoriesToFilter } }
                    .sortedByDescending { it.voteAverage }
                    .take(COUNT_NEXT_ITEMS_TO_WATCH)
                    .map { item ->
                        DisplayedItem(
                            item = item,
                            thumbnailUrl = item.getThumbnailUrl(configuration),
                        )
                    }
            }
        }

    companion object {
        const val COUNT_NEXT_ITEMS_TO_WATCH = 3
    }
}