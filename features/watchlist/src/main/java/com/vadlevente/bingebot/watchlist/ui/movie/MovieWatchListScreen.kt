package com.vadlevente.bingebot.watchlist.ui.movie

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.resources.R.string
import com.vadlevente.bingebot.watchlist.ui.ItemWatchListResources
import com.vadlevente.bingebot.watchlist.ui.ItemWatchListScreen
import com.vadlevente.bingebot.watchlist.viewmodel.movie.MovieWatchListViewModel

@Composable
fun MovieWatchListScreen(
    watchListId: String,
    viewModel: MovieWatchListViewModel = hiltViewModel(),
) {
    ItemWatchListScreen(
        watchListId = watchListId,
        viewModel = viewModel,
        resources = ItemWatchListResources(
            searchFieldHint = string.searchField_movieHint,
            emptyList = string.emptyList_movie_Description,
        )
    )
}
