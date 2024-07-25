package com.vadlevente.bingebot.bottomsheet.domain.usecases.movie

import com.vadlevente.bingebot.core.data.repository.ItemRepository
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.ui.BaseUseCase
import java.util.Date
import javax.inject.Inject

data class SetItemSeenUseCaseParams(
    val itemId: Int,
    val watchedDate: Date?,
)

class SetItemSeenUseCase <T : Item> @Inject constructor(
    private val itemRepository: ItemRepository<T>,
) : BaseUseCase<SetItemSeenUseCaseParams, Unit> {

    override fun execute(params: SetItemSeenUseCaseParams) = emptyFlow {
        itemRepository.setItemWatchedDate(params.itemId, params.watchedDate)
    }

}