package com.vadlevente.bingebot.watchlist.domain.usecase

import com.vadlevente.bingebot.core.data.repository.ItemRepository
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.model.WatchList
import com.vadlevente.bingebot.core.model.exception.BingeBotException
import com.vadlevente.bingebot.core.model.exception.Reason.DATA_NOT_FOUND
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class GetWatchListUseCaseParams(
    val watchListId: String,
)

class GetWatchListUseCase <T : Item> @Inject constructor(
    private val itemRepository: ItemRepository<T>,
) : BaseUseCase<GetWatchListUseCaseParams, WatchList> {

    override fun execute(params: GetWatchListUseCaseParams): Flow<WatchList> =
        itemRepository.getWatchLists().map {
            it.firstOrNull {
                it.watchListId == params.watchListId
            } ?: throw BingeBotException(reason = DATA_NOT_FOUND)
        }

}