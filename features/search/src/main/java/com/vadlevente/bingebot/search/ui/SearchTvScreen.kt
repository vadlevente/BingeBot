package com.vadlevente.bingebot.search.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.search.R
import com.vadlevente.bingebot.search.SearchTvViewModel

@Composable
fun SearchTvScreen(
    viewModel: SearchTvViewModel = hiltViewModel(),
) {
    SearchItemScreen(
        viewModel = viewModel,
        resources = SearchItemScreenResources(
            title = R.string.searchTv_pageTitle,
            searchFieldHint = R.string.searchTv_searchFieldHint,
            queryDescription = R.string.searchTv_shortQueryDescription,
        )
    )
}