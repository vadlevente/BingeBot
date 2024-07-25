package com.vadlevente.bingebot.search.usecase

import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.data.repository.ItemRepository
import com.vadlevente.bingebot.core.model.ApiConfiguration
import com.vadlevente.bingebot.core.model.DisplayedItem
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSearchResultUseCase <T : Item> @Inject constructor(
    private val itemRepository: ItemRepository<T>,
    private val preferencesDataSource: PreferencesDataSource,
) : BaseUseCase<Unit, List<DisplayedItem<T>>> {

    override fun execute(params: Unit) =
        combine(
            itemRepository.getSearchResult(),
            preferencesDataSource.apiConfiguration,
            ::Pair,
        ).map { (movies, configuration) ->
            movies.map { movie ->
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