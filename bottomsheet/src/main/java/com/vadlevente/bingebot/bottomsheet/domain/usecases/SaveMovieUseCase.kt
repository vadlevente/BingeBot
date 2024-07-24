package com.vadlevente.bingebot.bottomsheet.domain.usecases

import com.vadlevente.bingebot.core.data.repository.MovieRepository
import com.vadlevente.bingebot.core.model.Movie
import com.vadlevente.bingebot.core.ui.BaseUseCase
import javax.inject.Inject

data class SaveMovieUseCaseParams(
    val movie: Movie,
)

class SaveMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) : BaseUseCase<SaveMovieUseCaseParams, Unit>() {

    override fun execute(params: SaveMovieUseCaseParams) = emptyFlow {
        movieRepository.saveMovie(params.movie)
    }

}