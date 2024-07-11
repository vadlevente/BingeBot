package com.vadlevente.bingebot.core.data.cache

import com.vadlevente.bingebot.core.model.Genre
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface SelectedGenresCacheDataSource {
    val genresState: StateFlow<List<Genre>>
    fun updateGenres(genres: List<Genre>)
}
class SelectedGenresCacheDataSourceImpl @Inject constructor() : SelectedGenresCacheDataSource {
    private var _genresState = MutableStateFlow(emptyList<Genre>())
    override val genresState = _genresState.asStateFlow()

    override fun updateGenres(genres: List<Genre>) {
        _genresState.update { genres }
    }
}