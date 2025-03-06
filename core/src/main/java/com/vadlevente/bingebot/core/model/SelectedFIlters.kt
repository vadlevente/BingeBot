package com.vadlevente.bingebot.core.model

data class SelectedFilters(
    val genres: List<Genre> = emptyList(),
    val isWatched: Boolean? = null,
    val query: String? = null,
    val orderBy: OrderBy = OrderBy.DATE_ADDED_DESCENDING
)
