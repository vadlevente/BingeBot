package com.vadlevente.bingebot.list.domain.usecase

import com.vadlevente.bingebot.core.data.cache.SelectedFiltersCacheDataSource
import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.data.repository.ItemRepository
import com.vadlevente.bingebot.core.model.ApiConfiguration
import com.vadlevente.bingebot.core.model.DisplayedItem
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetItemsUseCase <T : Item> @Inject constructor(
    private val itemRepository: ItemRepository<T>,
    private val preferencesDataSource: PreferencesDataSource,
    private val selectedFiltersCacheDataSource: SelectedFiltersCacheDataSource,
) : BaseUseCase<Unit, List<DisplayedItem<T>>> {

    override fun execute(params: Unit): Flow<List<DisplayedItem<T>>> =
        combine(
            itemRepository.getItems(),
            preferencesDataSource.apiConfiguration,
            selectedFiltersCacheDataSource.filtersState,
            ::Triple,
        ).map { (items, configuration, filters) ->
            items
                .filter { movie ->
                    if (filters.genres.isEmpty()) true
                    else movie.genreCodes.any { filters.genres.map { it.id }.contains(it) }
                }
                .filter { movie ->
                    filters.isWatched?.let { isWatched ->
                        if (isWatched) {
                            movie.watchedDate != null
                        } else {
                            movie.watchedDate == null
                        }
                    } ?: true
                }
                .filter { movie ->
                    filters.query?.let { query ->
                        if (query.isEmpty()) return@let false
                        movie.title.lowercase().contains(query.lowercase()) ||
                            movie.originalTitle.lowercase().contains(query.lowercase())
                    } ?: true
                }
                .map { movie ->
                    DisplayedItem(
                        item = movie,
                        thumbnailUrl = getThumbnailUrl(configuration, movie),
                    )
                }
        }

    private fun getThumbnailUrl(
        configuration: ApiConfiguration,
        item: T,
    ) = item.posterPath?.let {
        "${configuration.imageConfiguration.thumbnailBaseUrl}${it}"
    }

}