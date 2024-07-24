package com.vadlevente.bingebot.list.domain.usecase.movie

import com.vadlevente.bingebot.core.data.repository.MovieRepository
import com.vadlevente.bingebot.list.domain.usecase.UpdateItemsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) : UpdateItemsUseCase {

    override fun execute(params: Unit): Flow<Unit> = emptyFlow {
        movieRepository.updateMovies()
    }

}