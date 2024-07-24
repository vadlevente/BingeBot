package com.vadlevente.bingebot.bottomsheet.domain.usecases.movie

import com.vadlevente.bingebot.bottomsheet.domain.usecases.RemoveItemFromWatchListUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.RemoveItemFromWatchListUseCaseParams
import com.vadlevente.bingebot.core.data.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoveMovieFromWatchListUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) : RemoveItemFromWatchListUseCase {

    override fun execute(params: RemoveItemFromWatchListUseCaseParams): Flow<Unit> = emptyFlow {
        movieRepository.removeMovieFromList(params.itemId, params.watchListId)
    }

}