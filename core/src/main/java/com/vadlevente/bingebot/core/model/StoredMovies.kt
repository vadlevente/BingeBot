package com.vadlevente.bingebot.core.model

data class StoredMovies(
    val movies: List<Movie>,
    val watchLists: List<WatchList>
)
