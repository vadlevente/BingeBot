package com.vadlevente.bingebot.list.domain.usecase.movie

import com.vadlevente.bingebot.core.data.repository.MovieRepository
import com.vadlevente.bingebot.list.domain.usecase.UpdateWatchListsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateMovieWatchListsUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) : UpdateWatchListsUseCase {

    override fun execute(params: Unit): Flow<Unit> = emptyFlow {
        movieRepository.updateWatchLists()
    }

}