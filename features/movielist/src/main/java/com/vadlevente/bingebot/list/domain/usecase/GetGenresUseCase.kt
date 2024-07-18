package com.vadlevente.bingebot.list.domain.usecase

import com.vadlevente.bingebot.core.data.cache.SelectedGenresCacheDataSource
import com.vadlevente.bingebot.core.data.repository.MovieRepository
import com.vadlevente.bingebot.core.ui.BaseUseCase
import com.vadlevente.bingebot.list.domain.model.DisplayedGenre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetGenresUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val selectedGenresCacheDataSource: SelectedGenresCacheDataSource,
) : BaseUseCase<Unit, List<DisplayedGenre>>() {

    override fun execute(params: Unit): Flow<List<DisplayedGenre>> =
        combine(
            movieRepository.getMovies(),
            movieRepository.getGenres(),
            selectedGenresCacheDataSource.genresState,
            ::Triple,
        ).map { (movies, genres, selectedGenres) ->
            genres.filter { genre ->
                movies.any { it.genreCodes.contains(genre.id) }
            }.map {
                DisplayedGenre(
                    it,
                    selectedGenres.map { it.id }.contains(it.id),
                )
            }
        }

}