package com.vadlevente.bingebot.list.domain.usecase

import com.vadlevente.bingebot.core.data.cache.SelectedFiltersCacheDataSource
import com.vadlevente.bingebot.core.data.repository.MovieRepository
import com.vadlevente.bingebot.core.ui.BaseUseCase
import com.vadlevente.bingebot.list.domain.model.DisplayedFilters
import com.vadlevente.bingebot.list.domain.model.DisplayedGenre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFiltersUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val selectedFiltersCacheDataSource: SelectedFiltersCacheDataSource,
) : BaseUseCase<Unit, DisplayedFilters>() {

    override fun execute(params: Unit): Flow<DisplayedFilters> =
        combine(
            movieRepository.getMovies(),
            movieRepository.getGenres(),
            selectedFiltersCacheDataSource.filtersState,
            ::Triple,
        ).map { (movies, genres, selectedFilters) ->
            genres.filter { genre ->
                movies.any { it.genreCodes.contains(genre.id) }
            }.map {
                DisplayedGenre(
                    it,
                    selectedFilters.genres.map { it.id }.contains(it.id),
                )
            }.let {
                DisplayedFilters(
                    it,
                    selectedFilters.isWatched,
                )
            }
        }

}