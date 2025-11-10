package com.vadlevente.bingebot.search.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.search.R
import com.vadlevente.bingebot.search.SearchTvViewModel
import com.vadlevente.bingebot.resources.R as Resources

@Composable
fun SearchTvScreen(
    viewModel: SearchTvViewModel = hiltViewModel(),
) {
    SearchItemScreen(
        viewModel = viewModel,
        resources = SearchItemScreenResources(
            title = Resources.string.searchTv_pageTitle,
            searchFieldHint = R.string.searchTv_searchFieldHint,
            queryDescription = R.string.searchTv_shortQueryDescription,
        )
    )
}