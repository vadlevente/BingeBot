package com.vadlevente.bingebot.bottomsheet.domain.usecases

import com.vadlevente.bingebot.core.data.repository.ItemRepository
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

data class CreateWatchListUseCaseParams(
    val title: String,
)

class CreateWatchListUseCase <T : Item> @Inject constructor(
    private val itemRepository: ItemRepository<T>,
) : BaseUseCase<CreateWatchListUseCaseParams, String> {

    override fun execute(params: CreateWatchListUseCaseParams): Flow<String> = flow {
        val watchListId = itemRepository.createWatchList(params.title)
        emit(watchListId)
    }

}