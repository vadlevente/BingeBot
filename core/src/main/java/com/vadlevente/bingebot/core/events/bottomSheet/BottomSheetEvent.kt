package com.vadlevente.bingebot.core.events.bottomSheet

sealed interface BottomSheetEvent {
    data class ShowMovieBottomSheet(
        val movieId: Int,
        val title: String,
        val thumbnailUrl: String?,
        val releaseYear: String,
    ) : BottomSheetEvent
}