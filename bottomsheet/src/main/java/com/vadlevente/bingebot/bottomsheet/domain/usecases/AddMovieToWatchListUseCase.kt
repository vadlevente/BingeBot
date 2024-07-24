package com.vadlevente.bingebot.bottomsheet.domain.usecases

import com.vadlevente.bingebot.core.data.repository.MovieRepository
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

data class AddMovieToWatchListUseCaseParams(
    val movieId: Int,
    val watchListId: String,
)

class AddMovieToWatchListUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) : BaseUseCase<AddMovieToWatchListUseCaseParams, Boolean>() {

    override fun execute(params: AddMovieToWatchListUseCaseParams): Flow<Boolean> = flow {
        val isNewlyAdded = movieRepository.addMovieToList(params.movieId, params.watchListId)
        emit(isNewlyAdded)
    }

}