package com.vadlevente.bingebot.core.usecase

import com.vadlevente.bingebot.core.data.repository.MovieRepository
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class RemoveMovieFromWatchListUseCaseParams(
    val movieId: Int,
    val watchListId: String,
)

class RemoveMovieFromWatchListUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) : BaseUseCase<RemoveMovieFromWatchListUseCaseParams, Unit>() {

    override fun execute(params: RemoveMovieFromWatchListUseCaseParams): Flow<Unit> = emptyFlow {
        movieRepository.removeMovieFromList(params.movieId, params.watchListId)
    }

}