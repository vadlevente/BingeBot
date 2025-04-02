package com.vadlevente.moviedetails.domain.usecases

import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.data.repository.ItemRepository
import com.vadlevente.bingebot.core.model.DisplayedItem
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.usecase.BaseUseCase
import com.vadlevente.bingebot.core.util.getProfileUrl
import com.vadlevente.bingebot.core.util.getThumbnailUrl
import com.vadlevente.moviedetails.domain.model.DisplayedItemDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
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
            ::Triple
        ).map { (configuration, details, genres) ->
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
            )
        }
}