package com.vadlevente.bingebot.watchlist.domain.usecase

import com.vadlevente.bingebot.core.data.repository.ItemRepository
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class DeleteWatchListUseCaseParams(
    val watchListId: String,
)

class DeleteWatchListUseCase <T : Item> @Inject constructor(
    private val itemRepository: ItemRepository<T>,
) : BaseUseCase<DeleteWatchListUseCaseParams, Unit> {

    override fun execute(params: DeleteWatchListUseCaseParams): Flow<Unit> = emptyFlow {
        itemRepository.deleteWatchList(params.watchListId)
    }

}