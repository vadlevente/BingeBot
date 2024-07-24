package com.vadlevente.bingebot.list.domain.model

data class DisplayedFilters(
    val displayedGenres: List<DisplayedGenre>,
    val isWatchedSelected: Boolean?,
)
