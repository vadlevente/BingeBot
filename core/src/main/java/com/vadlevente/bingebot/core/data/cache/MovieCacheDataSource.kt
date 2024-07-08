package com.vadlevente.bingebot.core.data.cache

import com.vadlevente.bingebot.core.model.Movie
import com.vadlevente.bingebot.core.model.SelectedLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class MovieCacheDataSource @Inject constructor() {

    private var _moviesState = MutableStateFlow(emptyList<Movie>())
    val moviesState = _moviesState.asStateFlow()

    fun updateMovies(movies: List<Movie>, language: SelectedLanguage) {
        _moviesState.update { movies.map { it.copy(localeCode = language.code)  } }
    }

}