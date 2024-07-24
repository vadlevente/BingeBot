package com.vadlevente.bingebot.search.usecase

import com.vadlevente.bingebot.core.data.repository.MovieRepository
import com.vadlevente.bingebot.core.ui.BaseUseCase
import javax.inject.Inject

data class SearchMovieUseCaseParams(
    val query: String,
)

class SearchMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) : BaseUseCase<SearchMovieUseCaseParams, Unit> {

    override fun execute(params: SearchMovieUseCaseParams) = emptyFlow {
        movieRepository.searchMovies(params.query)
    }

}