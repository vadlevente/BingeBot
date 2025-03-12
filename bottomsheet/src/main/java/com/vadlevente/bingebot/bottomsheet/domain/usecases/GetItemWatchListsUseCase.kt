package com.vadlevente.bingebot.bottomsheet.domain.usecases

import com.vadlevente.bingebot.core.data.repository.ItemRepository
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.model.WatchList
import com.vadlevente.bingebot.core.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class GetWatchListsUseCaseParams(
    val itemId: Int? = null,
)

class GetItemWatchListsUseCase <T : Item> @Inject constructor(
    private val itemRepository: ItemRepository<T>,
) : BaseUseCase<GetWatchListsUseCaseParams, List<WatchList>> {

    override fun execute(params: GetWatchListsUseCaseParams): Flow<List<WatchList>> =
        itemRepository.getWatchLists().map {
            it.filter {
                params.itemId?.let { itemId ->
                    !it.itemIds.contains(itemId)
                } != false
            }
        }

}