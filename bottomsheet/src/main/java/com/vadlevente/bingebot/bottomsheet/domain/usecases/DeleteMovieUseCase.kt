package com.vadlevente.bingebot.bottomsheet.domain.usecases

import com.vadlevente.bingebot.core.data.repository.MovieRepository
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class DeleteMovieUseCaseParams(
    val movieId: Int,
)

class DeleteMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) : BaseUseCase<DeleteMovieUseCaseParams, Unit>() {

    override fun execute(params: DeleteMovieUseCaseParams): Flow<Unit> = emptyFlow {
        movieRepository.deleteMovie(params.movieId)
    }

}