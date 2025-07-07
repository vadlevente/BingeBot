package com.vadlevente.moviedetails.domain.usecases

import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.data.repository.ItemRepository
import com.vadlevente.bingebot.core.model.DisplayedItem
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.usecase.BaseUseCase
import com.vadlevente.bingebot.core.util.getLogoUrl
import com.vadlevente.bingebot.core.util.getProfileUrl
import com.vadlevente.bingebot.core.util.getThumbnailUrl
import com.vadlevente.moviedetails.domain.model.DisplayedItemDetails
import com.vadlevente.moviedetails.domain.model.DisplayedProviderInfo
import com.vadlevente.moviedetails.domain.model.DisplayedWatchProviders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class GetItemDetailsUseCaseParams(
    val itemId: Int,
)

class GetItemDetailsUseCase<T : Item> @Inject constructor(
    private val itemRepository: ItemRepository<T>,
    private val preferencesDataSource: PreferencesDataSource,
) : BaseUseCase<GetItemDetailsUseCaseParams, DisplayedItemDetails<T>> {

    override fun execute(params: GetItemDetailsUseCaseParams): Flow<DisplayedItemDetails<T>> =
        combine(
            preferencesDataSource.apiConfiguration,
            itemRepository.getItemDetails(params.itemId),
            itemRepository.getGenres(),
            itemRepository.getWatchProviders(params.itemId)
        ) { configuration, details, genres, providers ->
            DisplayedItemDetails(
                displayedItem = DisplayedItem(
                    item = details.item,
                    thumbnailUrl = details.item.getThumbnailUrl(configuration),
                ),
                credits = details.credits.copy(
                    cast = details.credits.cast.map { castMember ->
                        castMember.copy(
                            profileUrl = castMember.getProfileUrl(configuration)
                        )
                    }
                ),
                genres = genres.filter {
                    details.item.genreCodes.contains(it.id)
                },
                providers = DisplayedWatchProviders(
                    flatrate = providers?.flatrate?.map {
                        DisplayedProviderInfo(
                            name = it.name,
                            fullPath = it.getLogoUrl(configuration)
                        )
                    } ?: emptyList(),
                    buy = providers?.buy?.map {
                        DisplayedProviderInfo(
                            name = it.name,
                            fullPath = it.getLogoUrl(configuration)
                        )
                    } ?: emptyList(),
                    rent = providers?.rent?.map {
                        DisplayedProviderInfo(
                            name = it.name,
                            fullPath = it.getLogoUrl(configuration)
                        )
                    } ?: emptyList(),
                ),
            )
        }
}