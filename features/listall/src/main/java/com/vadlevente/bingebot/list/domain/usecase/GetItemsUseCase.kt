package com.vadlevente.bingebot.list.domain.usecase

import com.vadlevente.bingebot.core.data.cache.SelectedFiltersCacheDataSource
import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.data.repository.ItemRepository
import com.vadlevente.bingebot.core.model.DisplayedItem
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.model.OrderBy
import com.vadlevente.bingebot.core.ui.BaseUseCase
import com.vadlevente.bingebot.core.util.getThumbnailUrl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetItemsUseCase <T : Item> @Inject constructor(
    private val itemRepository: ItemRepository<T>,
    private val preferencesDataSource: PreferencesDataSource,
    private val selectedFiltersCacheDataSource: SelectedFiltersCacheDataSource<T>,
) : BaseUseCase<Unit, List<DisplayedItem<T>>> {

    override fun execute(params: Unit): Flow<List<DisplayedItem<T>>> =
        combine(
            itemRepository.getItems(),
            preferencesDataSource.apiConfiguration,
            selectedFiltersCacheDataSource.filtersState,
            ::Triple,
        ).map { (items, configuration, filters) ->
            items
                .filter { item ->
                    if (filters.genres.isEmpty()) true
                    else item.genreCodes.any { filters.genres.map { it.id }.contains(it) }
                }
                .filter { item ->
                    filters.isWatched?.let { isWatched ->
                        if (isWatched) {
                            item.watchedDate != null
                        } else {
                            item.watchedDate == null
                        }
                    } != false
                }
                .filter { item ->
                    filters.query?.let { query ->
                        if (query.isEmpty()) return@let false
                        item.title.lowercase().contains(query.lowercase()) ||
                            item.originalTitle.lowercase().contains(query.lowercase())
                    } != false
                }
                .map { item ->
                    DisplayedItem(
                        item = item,
                        thumbnailUrl = item.getThumbnailUrl(configuration),
                    )
                }
                .let { items ->
                    when (filters.orderBy) {
                        OrderBy.DATE_ADDED_ASCENDING -> items.sortedBy { it.item.createdDate }
                        OrderBy.DATE_ADDED_DESCENDING -> items.sortedByDescending { it.item.createdDate }
                        OrderBy.RATING_ASCENDING -> items.sortedBy { it.item.voteAverage }
                        OrderBy.RATING_DESCENDING -> items.sortedByDescending { it.item.voteAverage }
                    }
                }
        }

}