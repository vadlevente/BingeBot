package com.vadlevente.bingebot.bottomsheet.domain.usecases.movie

import com.vadlevente.bingebot.core.data.repository.ItemRepository
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class RemoveItemFromWatchListUseCaseParams(
    val itemId: Int,
    val watchListId: String,
)

class RemoveItemFromWatchListUseCase <T : Item> @Inject constructor(
    private val itemRepository: ItemRepository<T>,
) : BaseUseCase<RemoveItemFromWatchListUseCaseParams, Unit> {

    override fun execute(params: RemoveItemFromWatchListUseCaseParams): Flow<Unit> = emptyFlow {
        itemRepository.removeItemFromList(params.itemId, params.watchListId)
    }

}