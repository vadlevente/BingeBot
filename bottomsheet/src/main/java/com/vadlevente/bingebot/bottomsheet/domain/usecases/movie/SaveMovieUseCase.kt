package com.vadlevente.bingebot.bottomsheet.domain.usecases.movie

import com.vadlevente.bingebot.bottomsheet.domain.usecases.SaveItemUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.SaveItemUseCaseParams
import com.vadlevente.bingebot.core.data.repository.MovieRepository
import com.vadlevente.bingebot.core.model.Movie
import javax.inject.Inject

class SaveMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) : SaveItemUseCase {

    override fun execute(params: SaveItemUseCaseParams) = emptyFlow {
        movieRepository.saveMovie(params.item as Movie)
    }

}