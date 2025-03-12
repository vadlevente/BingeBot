package com.vadlevente.bingebot.bottomsheet.domain.usecases

import com.vadlevente.bingebot.core.data.repository.ItemRepository
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.usecase.BaseUseCase
import javax.inject.Inject

data class SaveItemUseCaseParams <T : Item>(
    val item: T,
)

class SaveItemUseCase <T : Item> @Inject constructor(
    private val itemRepository: ItemRepository<T>,
) : BaseUseCase<SaveItemUseCaseParams<T>, Unit> {

    override fun execute(params: SaveItemUseCaseParams<T>) = emptyFlow {
        itemRepository.saveItem(params.item)
    }

}