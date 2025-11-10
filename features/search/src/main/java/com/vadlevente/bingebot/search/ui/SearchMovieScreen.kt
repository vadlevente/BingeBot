package com.vadlevente.bingebot.search.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.search.R
import com.vadlevente.bingebot.search.SearchMovieViewModel
import com.vadlevente.bingebot.resources.R as Resources

@Composable
fun SearchMovieScreen(
    viewModel: SearchMovieViewModel = hiltViewModel(),
) {
    SearchItemScreen(
        viewModel = viewModel,
        resources = SearchItemScreenResources(
            title = Resources.string.searchMovies_pageTitle,
            searchFieldHint = R.string.searchMovies_searchFieldHint,
            queryDescription = R.string.searchMovies_shortQueryDescription,
        )
    )
}