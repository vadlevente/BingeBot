package com.vadlevente.bingebot.core.model

data class DisplayedMovie(
    override val item: Movie,
    override val thumbnailUrl: String?,
) : DisplayedItem<Movie>
