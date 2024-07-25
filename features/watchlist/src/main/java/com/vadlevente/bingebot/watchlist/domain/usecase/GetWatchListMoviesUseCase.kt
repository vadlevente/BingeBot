package com.vadlevente.bingebot.watchlist.domain.usecase

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

data class GetWatchListItemsUseCaseParams(
    val watchListId: String,
    val query: String? = null,
)

class GetWatchListMoviesUseCase <T : Item> @Inject constructor(
    private val itemRepository: ItemRepository<T>,
    private val preferencesDataSource: PreferencesDataSource,
) : BaseUseCase<GetWatchListItemsUseCaseParams, List<DisplayedItem<T>>> {

    override fun execute(params: GetWatchListItemsUseCaseParams): Flow<List<DisplayedItem<T>>> =
        combine(
            itemRepository.getWatchListItems(params.watchListId),
            preferencesDataSource.apiConfiguration,
            ::Pair,
        ).map { (movies, configuration) ->
            movies
                .filter { movie ->
                    params.query?.let { query ->
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