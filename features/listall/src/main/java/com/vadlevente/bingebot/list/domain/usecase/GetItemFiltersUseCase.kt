package com.vadlevente.bingebot.list.domain.usecase

import com.vadlevente.bingebot.core.data.cache.SelectedFiltersCacheDataSource
import com.vadlevente.bingebot.core.data.repository.ItemRepository
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.usecase.BaseUseCase
import com.vadlevente.bingebot.list.domain.model.DisplayedFilters
import com.vadlevente.bingebot.list.domain.model.DisplayedGenre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetItemFiltersUseCase <T : Item> @Inject constructor(
    private val itemRepository: ItemRepository<T>,
    private val selectedFiltersCacheDataSource: SelectedFiltersCacheDataSource<T>,
) : BaseUseCase<Unit, DisplayedFilters> {

    override fun execute(params: Unit): Flow<DisplayedFilters> =
        combine(
            itemRepository.getItems(),
            itemRepository.getGenres(),
            selectedFiltersCacheDataSource.filtersState,
            ::Triple,
        ).map { (items, genres, selectedFilters) ->
            genres.filter { genre ->
                items.any { it.genreCodes.contains(genre.id) }
            }.map {
                DisplayedGenre(
                    it,
                    selectedFilters.genres.map { it.id }.contains(it.id),
                )
            }.let {
                DisplayedFilters(
                    it,
                    selectedFilters.isWatched,
                    selectedFilters.query,
                )
            }
        }

}