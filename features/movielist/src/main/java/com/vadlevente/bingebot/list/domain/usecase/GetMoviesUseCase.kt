package com.vadlevente.bingebot.list.domain.usecase

import com.vadlevente.bingebot.core.data.cache.SelectedGenresCacheDataSource
import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.data.repository.MovieRepository
import com.vadlevente.bingebot.core.model.ApiConfiguration
import com.vadlevente.bingebot.core.model.DisplayedMovie
import com.vadlevente.bingebot.core.model.Movie
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class GetMoviesUseCaseParams(
    val query: String? = null,
)

class GetMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val preferencesDataSource: PreferencesDataSource,
    private val selectedGenresCacheDataSource: SelectedGenresCacheDataSource,
) : BaseUseCase<GetMoviesUseCaseParams, List<DisplayedMovie>>() {

    override fun execute(params: GetMoviesUseCaseParams): Flow<List<DisplayedMovie>> =
        combine(
            movieRepository.getMovies(),
            preferencesDataSource.apiConfiguration,
            selectedGenresCacheDataSource.genresState,
            ::Triple,
        ).map { (movies, configuration, genres) ->
            movies
                .filter { movie ->
                    if (genres.isEmpty()) true
                    else movie.genreCodes.any { genres.map { it.id }.contains(it) }
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