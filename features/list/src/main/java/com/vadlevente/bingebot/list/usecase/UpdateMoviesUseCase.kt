package com.vadlevente.bingebot.list.usecase

import com.vadlevente.bingebot.core.data.repository.MovieRepository
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) : BaseUseCase<Unit, Unit>() {

    override fun execute(params: Unit): Flow<Unit> = emptyFlow {
        movieRepository.updateMovies()
    }

}