package com.vadlevente.bingebot.bottomsheet.domain.usecases

import com.vadlevente.bingebot.core.data.repository.ItemRepository
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class DeleteItemUseCaseParams(
    val itemId: Int,
)

class DeleteItemUseCase <T : Item> @Inject constructor(
    private val itemRepository: ItemRepository<T>,
) : BaseUseCase<DeleteItemUseCaseParams, Unit> {

    override fun execute(params: DeleteItemUseCaseParams): Flow<Unit> = emptyFlow {
        itemRepository.deleteItem(params.itemId)
    }

}