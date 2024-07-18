package com.vadlevente.bingebot.watchlist.domain.usecase

import com.vadlevente.bingebot.core.data.repository.MovieRepository
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

class GetWatchListUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) : BaseUseCase<GetWatchListUseCaseParams, WatchList>() {

    override fun execute(params: GetWatchListUseCaseParams): Flow<WatchList> =
        movieRepository.getWatchLists().map {
            it.firstOrNull {
                it.watchListId == params.watchListId
            } ?: throw BingeBotException(reason = DATA_NOT_FOUND)
        }

}