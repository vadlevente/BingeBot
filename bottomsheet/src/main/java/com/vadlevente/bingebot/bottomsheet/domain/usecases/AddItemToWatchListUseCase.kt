package com.vadlevente.bingebot.bottomsheet.domain.usecases

import com.vadlevente.bingebot.core.data.repository.ItemRepository
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

data class AddItemToWatchListUseCaseParams(
    val itemId: Int,
    val watchListId: String,
)

class AddItemToWatchListUseCase <T : Item> @Inject constructor(
    private val itemRepository: ItemRepository<T>,
) : BaseUseCase<AddItemToWatchListUseCaseParams, Boolean> {

    override fun execute(params: AddItemToWatchListUseCaseParams): Flow<Boolean> = flow {
        val isNewlyAdded = itemRepository.addItemToList(params.itemId, params.watchListId)
        emit(isNewlyAdded)
    }

}