package com.vadlevente.bingebot.list.usecase

import com.vadlevente.bingebot.core.data.repository.MovieRepository
import com.vadlevente.bingebot.core.model.Genre
import com.vadlevente.bingebot.core.model.Movie
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class GetMoviesUseCaseParams(
    val genres: List<Genre> = emptyList(),
)

class GetMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) : BaseUseCase<GetMoviesUseCaseParams, List<Movie>>() {

    override fun execute(params: GetMoviesUseCaseParams): Flow<List<Movie>> =
        movieRepository.getMovies().map {
            it.filter { movie ->
                if (params.genres.isEmpty()) true
                else movie.genreCodes.any { params.genres.map { it.id }.contains(it) }
            }
        }

}