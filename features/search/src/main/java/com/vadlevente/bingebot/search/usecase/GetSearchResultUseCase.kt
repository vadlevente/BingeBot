package com.vadlevente.bingebot.search.usecase

import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.data.repository.MovieRepository
import com.vadlevente.bingebot.core.model.ApiConfiguration
import com.vadlevente.bingebot.core.model.DisplayedItem
import com.vadlevente.bingebot.core.model.Movie
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSearchResultUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val preferencesDataSource: PreferencesDataSource,
) : BaseUseCase<Unit, List<DisplayedItem<Movie>>> {

    override fun execute(params: Unit) =
        combine(
            movieRepository.getSearchResult(),
            preferencesDataSource.apiConfiguration,
            ::Pair,
        ).map { (movies, configuration) ->
            movies.map { movie ->
                DisplayedItem(
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