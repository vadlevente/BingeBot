package com.vadlevente.bingebot.bottomsheet.domain.usecases.movie

import com.vadlevente.bingebot.bottomsheet.domain.usecases.DeleteItemUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.DeleteItemUseCaseParams
import com.vadlevente.bingebot.core.data.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class DeleteMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) : DeleteItemUseCase {

    override fun execute(params: DeleteItemUseCaseParams): Flow<Unit> = emptyFlow {
        movieRepository.deleteMovie(params.itemId)
    }

}