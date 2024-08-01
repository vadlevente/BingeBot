package com.vadlevente.bingebot.watchlist.ui.tv

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.resources.R.string
import com.vadlevente.bingebot.watchlist.ui.ItemWatchListResources
import com.vadlevente.bingebot.watchlist.ui.ItemWatchListScreen
import com.vadlevente.bingebot.watchlist.viewmodel.tv.TvWatchListViewModel

@Composable
fun TvWatchListScreen(
    watchListId: String,
    viewModel: TvWatchListViewModel = hiltViewModel(),
) {
    ItemWatchListScreen(
        watchListId = watchListId,
        viewModel = viewModel,
        resources = ItemWatchListResources(
            searchFieldHint = string.searchField_tvHint,
            emptyList = string.emptyList_tv_Description,
        )
    )
}
