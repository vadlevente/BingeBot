package com.vadlevente.bingebot.core.model

data class SelectedFilters(
    val genres: List<Genre> = emptyList(),
    val isWatched: Boolean? = null,
    val query: String? = null,
)
