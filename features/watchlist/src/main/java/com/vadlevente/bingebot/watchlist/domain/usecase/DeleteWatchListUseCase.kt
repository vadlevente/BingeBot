package com.vadlevente.bingebot.watchlist.domain.usecase

import com.vadlevente.bingebot.core.data.repository.MovieRepository
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class DeleteWatchListUseCaseParams(
    val watchListId: String,
)

class DeleteWatchListUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) : BaseUseCase<DeleteWatchListUseCaseParams, Unit> {

    override fun execute(params: DeleteWatchListUseCaseParams): Flow<Unit> = emptyFlow {
        movieRepository.deleteWatchList(params.watchListId)
    }

}