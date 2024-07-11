package com.vadlevente.bingebot.core.data.cache

import com.vadlevente.bingebot.core.model.Movie
import com.vadlevente.bingebot.core.model.SelectedLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface MovieCacheDataSource {
    val moviesState: StateFlow<List<Movie>>
    fun updateMovies(movies: List<Movie>, language: SelectedLanguage)
}

class MovieCacheDataSourceImpl @Inject constructor(): MovieCacheDataSource {

    private var _moviesState = MutableStateFlow(emptyList<Movie>())
    override val moviesState = _moviesState.asStateFlow()

    override fun updateMovies(movies: List<Movie>, language: SelectedLanguage) {
        _moviesState.update { movies.map { it.copy(localeCode = language.code)  } }
    }

}