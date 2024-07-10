package com.vadlevente.bingebot.core.usecase

import com.vadlevente.bingebot.core.data.repository.MovieRepository
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class AddMovieToWatchListUseCaseParams(
    val movieId: Int,
    val watchListId: String,
)

class AddMovieToWatchListUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) : BaseUseCase<AddMovieToWatchListUseCaseParams, Unit>() {

    override fun execute(params: AddMovieToWatchListUseCaseParams): Flow<Unit> = emptyFlow {
        movieRepository.addMovieToList(params.movieId, params.watchListId)
    }

}