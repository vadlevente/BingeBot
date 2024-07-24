package com.vadlevente.bingebot.watchlist.domain.usecase

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

data class GetWatchListMoviesUseCaseParams(
    val watchListId: String,
    val query: String? = null,
)

class GetWatchListMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val preferencesDataSource: PreferencesDataSource,
) : BaseUseCase<GetWatchListMoviesUseCaseParams, List<DisplayedMovie>> {

    override fun execute(params: GetWatchListMoviesUseCaseParams): Flow<List<DisplayedMovie>> =
        combine(
            movieRepository.getWatchListMovies(params.watchListId),
            preferencesDataSource.apiConfiguration,
            ::Pair,
        ).map { (movies, configuration) ->
            movies
                .filter { movie ->
                    params.query?.let { query ->
                        if (query.isEmpty()) return@let false
                        movie.title.lowercase().contains(query.lowercase()) ||
                            movie.originalTitle.lowercase().contains(query.lowercase())
                    } ?: true
                }
                .map { movie ->
                    DisplayedMovie(
                        item = movie,
                        thumbnailUrl = getThumbnailUrl(configuration, movie),
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