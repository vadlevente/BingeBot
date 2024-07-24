package com.vadlevente.bingebot.list.domain.usecase.movie

import com.vadlevente.bingebot.core.data.cache.SelectedFiltersCacheDataSource
import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.data.repository.MovieRepository
import com.vadlevente.bingebot.core.model.ApiConfiguration
import com.vadlevente.bingebot.core.model.DisplayedMovie
import com.vadlevente.bingebot.core.model.Movie
import com.vadlevente.bingebot.list.domain.usecase.GetItemsUseCase
import com.vadlevente.bingebot.list.domain.usecase.GetItemsUseCaseParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val preferencesDataSource: PreferencesDataSource,
    private val selectedFiltersCacheDataSource: SelectedFiltersCacheDataSource,
) : GetItemsUseCase<Movie> {

    override fun execute(params: GetItemsUseCaseParams): Flow<List<DisplayedMovie>> =
        combine(
            movieRepository.getMovies(),
            preferencesDataSource.apiConfiguration,
            selectedFiltersCacheDataSource.filtersState,
            ::Triple,
        ).map { (movies, configuration, filters) ->
            movies
                .filter { movie ->
                    if (filters.genres.isEmpty()) true
                    else movie.genreCodes.any { filters.genres.map { it.id }.contains(it) }
                }
                .filter { movie ->
                    filters.isWatched?.let { isWatched ->
                        if (isWatched) {
                            movie.watchedDate != null
                        } else {
                            movie.watchedDate == null
                        }
                    } ?: true
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