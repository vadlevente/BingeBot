package com.vadlevente.bingebot.core.data.cache

import com.vadlevente.bingebot.core.model.Genre
import com.vadlevente.bingebot.core.model.SelectedFilters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface SelectedFiltersCacheDataSource {
    val filtersState: StateFlow<SelectedFilters>
    fun updateGenres(genres: List<Genre>)
    fun updateIsWatched(value: Boolean?)
    fun updateQuery(value: String?)
}
class SelectedFiltersCacheDataSourceImpl @Inject constructor() : SelectedFiltersCacheDataSource {
    private var _filtersState = MutableStateFlow(SelectedFilters())
    override val filtersState = _filtersState.asStateFlow()

    override fun updateGenres(genres: List<Genre>) {
        _filtersState.update {
            it.copy(
                genres = genres,
            )
        }
    }

    override fun updateIsWatched(value: Boolean?) {
        _filtersState.update {
            it.copy(
                isWatched = value,
            )
        }
    }

    override fun updateQuery(value: String?) {
        _filtersState.update {
            it.copy(
                query = value,
            )
        }
    }
}