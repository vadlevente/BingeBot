package com.vadlevente.watchlist

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun WatchListScreen(
    watchListId: String,
) {
    Text("WatchList: $watchListId")
}