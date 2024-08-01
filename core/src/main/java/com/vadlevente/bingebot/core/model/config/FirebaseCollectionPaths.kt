package com.vadlevente.bingebot.core.model.config

import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.model.Item.Movie
import com.vadlevente.bingebot.core.model.Item.Tv

sealed interface FirebaseCollectionPaths<T: Item> {
    val ITEM_COLLECTION_PATH: String
    val ITEM_WATCHLIST_PATH: String

    object MovieFirebaseCollectionPaths : FirebaseCollectionPaths<Movie> {
        override val ITEM_COLLECTION_PATH: String = "movies"
        override val ITEM_WATCHLIST_PATH: String = "movieWatchLists"
    }

    object TvFirebaseCollectionPaths : FirebaseCollectionPaths<Tv> {
        override val ITEM_COLLECTION_PATH: String = "tvs"
        override val ITEM_WATCHLIST_PATH: String = "tvWatchLists"
    }
}

