package com.vadlevente.bingebot.bottomsheet.domain.usecases.movie

import com.vadlevente.bingebot.bottomsheet.domain.usecases.AddItemToWatchListUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.AddItemToWatchListUseCaseParams
import com.vadlevente.bingebot.core.data.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddMovieToWatchListUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) : AddItemToWatchListUseCase {

    override fun execute(params: AddItemToWatchListUseCaseParams): Flow<Boolean> = flow {
        val isNewlyAdded = movieRepository.addMovieToList(params.itemId, params.watchListId)
        emit(isNewlyAdded)
    }

}