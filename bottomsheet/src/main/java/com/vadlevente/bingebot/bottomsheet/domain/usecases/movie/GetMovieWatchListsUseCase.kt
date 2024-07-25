package com.vadlevente.bingebot.bottomsheet.domain.usecases.movie

import com.vadlevente.bingebot.core.data.repository.ItemRepository
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.model.WatchList
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class GetWatchListsUseCaseParams(
    val itemId: Int? = null,
)

class GetMovieWatchListsUseCase <T : Item> @Inject constructor(
    private val itemRepository: ItemRepository<T>,
) : BaseUseCase<GetWatchListsUseCaseParams, List<WatchList>> {

    override fun execute(params: GetWatchListsUseCaseParams): Flow<List<WatchList>> =
        itemRepository.getWatchLists().map {
            it.filter {
                params.itemId?.let { movieId ->
                    !it.itemIds.contains(movieId)
                } ?: true
            }
        }

}