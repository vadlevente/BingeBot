package com.vadlevente.bingebot.list.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.list.MovieListViewModel
import com.vadlevente.bingebot.list.R
import com.vadlevente.bingebot.resources.R as Res

@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel = hiltViewModel(),
) {
    ItemListScreen(
        viewModel = viewModel,
        resources = ItemListScreenResources(
            title = R.string.movieList_pageTitle,
            searchFieldHint = Res.string.searchField_movieHint,
            emptyListDescription = Res.string.emptyList_movie_Description,
        )
    )
}