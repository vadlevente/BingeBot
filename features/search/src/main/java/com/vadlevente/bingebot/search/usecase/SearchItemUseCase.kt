package com.vadlevente.bingebot.search.usecase

import com.vadlevente.bingebot.core.data.repository.ItemRepository
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.ui.BaseUseCase
import javax.inject.Inject

data class SearchItemUseCaseParams(
    val query: String,
)

class SearchItemUseCase <T : Item> @Inject constructor(
    private val itemRepository: ItemRepository<T>,
) : BaseUseCase<SearchItemUseCaseParams, Unit> {

    override fun execute(params: SearchItemUseCaseParams) = emptyFlow {
        itemRepository.searchItems(params.query)
    }

}