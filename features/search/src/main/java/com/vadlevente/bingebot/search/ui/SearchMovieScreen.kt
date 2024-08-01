package com.vadlevente.bingebot.search.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.search.R
import com.vadlevente.bingebot.search.SearchMovieViewModel

@Composable
fun SearchMovieScreen(
    viewModel: SearchMovieViewModel = hiltViewModel(),
) {
    SearchItemScreen(
        viewModel = viewModel,
        resources = SearchItemScreenResources(
            title = R.string.searchMovies_pageTitle,
            searchFieldHint = R.string.searchMovies_searchFieldHint,
            queryDescription = R.string.searchMovies_shortQueryDescription,
        )
    )
}