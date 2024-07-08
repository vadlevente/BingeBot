package com.vadlevente.bingebot.search.usecase

import com.vadlevente.bingebot.core.data.repository.MovieRepository
import com.vadlevente.bingebot.core.model.Movie
import com.vadlevente.bingebot.core.ui.BaseUseCase
import javax.inject.Inject

class GetSearchResultUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) : BaseUseCase<Unit, List<Movie>>() {

    override fun execute(params: Unit) = movieRepository.getSearchResult()

}