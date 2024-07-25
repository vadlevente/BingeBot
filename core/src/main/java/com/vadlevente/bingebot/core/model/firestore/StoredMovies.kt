package com.vadlevente.bingebot.core.model.firestore

import com.vadlevente.bingebot.core.model.WatchList

data class StoredData(
    val movies: List<StoredItem>,
    val movieWatchLists: List<WatchList>,
    val tvs: List<StoredItem>,
    val tvWatchLists: List<WatchList>,
) {
    constructor() : this(emptyList(), emptyList(), emptyList(), emptyList())
}
