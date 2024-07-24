package com.vadlevente.bingebot.bottomsheet.domain.usecases.movie

import com.vadlevente.bingebot.bottomsheet.domain.usecases.CreateWatchListUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.CreateWatchListUseCaseParams
import com.vadlevente.bingebot.core.data.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateMovieWatchListUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) : CreateWatchListUseCase {

    override fun execute(params: CreateWatchListUseCaseParams): Flow<String> = flow {
        val watchListId = movieRepository.createWatchList(params.title)
        emit(watchListId)
    }

}