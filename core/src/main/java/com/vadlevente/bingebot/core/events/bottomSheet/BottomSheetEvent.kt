package com.vadlevente.bingebot.core.events.bottomSheet

import com.vadlevente.bingebot.core.model.DisplayedMovie

sealed interface BottomSheetEvent {
    data class ShowMovieBottomSheet(
        val movie: DisplayedMovie,
        val alreadySaved: Boolean,
    ) : BottomSheetEvent

    data class ShowAddMovieToWatchListBottomSheet(
        val movie: DisplayedMovie,
        val alreadySaved: Boolean,
    ) : BottomSheetEvent

    object ShowWatchListsBottomSheet : BottomSheetEvent

}