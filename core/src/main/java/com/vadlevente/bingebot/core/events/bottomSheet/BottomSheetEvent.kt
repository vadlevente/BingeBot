package com.vadlevente.bingebot.core.events.bottomSheet

import com.vadlevente.bingebot.core.model.DisplayedItem
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.model.Movie

sealed interface BottomSheetEvent {

    sealed interface ShowItemBottomSheet<T: Item> : BottomSheetEvent {
        val item: DisplayedItem<T>
        val alreadySaved: Boolean
        val watchListId: String?
        data class ShowMovieBottomSheet(
            override val item: DisplayedItem<Movie>,
            override val alreadySaved: Boolean,
            override val watchListId: String? = null,
        ) : ShowItemBottomSheet<Movie>

    }

    data class ShowAddItemToWatchListBottomSheet(
        val movie: DisplayedItem<Movie>,
        val alreadySaved: Boolean,
    ) : BottomSheetEvent

    object ShowWatchListsBottomSheet : BottomSheetEvent

}