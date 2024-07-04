package com.vadlevente.bingebot.core.model.firestore

import com.vadlevente.bingebot.core.model.WatchList

data class StoredMovies(
    val movies: List<StoredMovie>,
    val watchLists: List<WatchList>
)
