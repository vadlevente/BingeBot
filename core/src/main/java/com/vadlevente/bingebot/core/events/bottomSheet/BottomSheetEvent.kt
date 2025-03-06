package com.vadlevente.bingebot.core.events.bottomSheet

import com.vadlevente.bingebot.core.model.DisplayedItem
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.model.Item.Movie
import com.vadlevente.bingebot.core.model.Item.Tv

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

        data class ShowTvBottomSheet(
            override val item: DisplayedItem<Tv>,
            override val alreadySaved: Boolean,
            override val watchListId: String? = null,
        ) : ShowItemBottomSheet<Tv>

    }

    sealed interface ShowAddItemToWatchListBottomSheet<T: Item> : BottomSheetEvent {
        val item: DisplayedItem<T>
        val alreadySaved: Boolean
        data class ShowAddMovieToWatchListBottomSheet(
            override val item: DisplayedItem<Movie>,
            override val alreadySaved: Boolean,
        ) : ShowAddItemToWatchListBottomSheet<Movie>

        data class ShowAddTvToWatchListBottomSheet(
            override val item: DisplayedItem<Tv>,
            override val alreadySaved: Boolean,
        ) : ShowAddItemToWatchListBottomSheet<Tv>

    }

    sealed interface ShowWatchListsBottomSheet<T: Item> : BottomSheetEvent {
        object ShowMovieWatchListsBottomSheet : ShowWatchListsBottomSheet<Movie>
        object ShowTvWatchListsBottomSheet : ShowWatchListsBottomSheet<Tv>
    }

    sealed interface ShowOrderByBottomSheet<T: Item> : BottomSheetEvent {
        object ShowMovieOrderByBottomSheet : ShowOrderByBottomSheet<Movie>
        object ShowTvOrderByBottomSheet : ShowOrderByBottomSheet<Tv>
    }

}