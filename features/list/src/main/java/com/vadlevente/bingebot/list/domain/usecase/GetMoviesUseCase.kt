package com.vadlevente.bingebot.list.domain.usecase

import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.data.repository.MovieRepository
import com.vadlevente.bingebot.core.model.ApiConfiguration
import com.vadlevente.bingebot.core.model.DisplayedMovie
import com.vadlevente.bingebot.core.model.Genre
import com.vadlevente.bingebot.core.model.Movie
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class GetMoviesUseCaseParams(
    val genres: List<Genre> = emptyList(),
    val query: String? = null,
)

class GetMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val preferencesDataSource: PreferencesDataSource,
) : BaseUseCase<GetMoviesUseCaseParams, List<DisplayedMovie>>() {

    override fun execute(params: GetMoviesUseCaseParams): Flow<List<DisplayedMovie>> =
        combine(
            movieRepository.getMovies(),
            preferencesDataSource.apiConfiguration,
            ::Pair,
        ).map { (movies, configuration) ->
            movies
                .filter { movie ->
                    if (params.genres.isEmpty()) true
                    else movie.genreCodes.any { params.genres.map { it.id }.contains(it) }
                }
                .filter { movie ->
                    params.query?.let { query ->
                        if (query.isEmpty()) return@let false
                        movie.title.lowercase().contains(query.lowercase()) ||
                            movie.originalTitle.lowercase().contains(query.lowercase())
                    } ?: true
                }
                .map { movie ->
                    DisplayedMovie(
                        movie = movie,
                        backdropUrl = getThumbnailUrl(configuration, movie),
                    )
                }
        }

    private fun getThumbnailUrl(
        configuration: ApiConfiguration,
        movie: Movie,
    ) = movie.posterPath?.let {
        "${configuration.imageConfiguration.thumbnailBaseUrl}${it}"
    }

}