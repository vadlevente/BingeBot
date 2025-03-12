package com.vadlevente.bingebot.list.domain.usecase

import com.vadlevente.bingebot.core.data.repository.ItemRepository
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateWatchListsUseCase <T : Item> @Inject constructor(
    private val itemRepository: ItemRepository<T>,
) : BaseUseCase<Unit, Unit> {

    override fun execute(params: Unit): Flow<Unit> = emptyFlow {
        itemRepository.updateWatchLists()
    }

}