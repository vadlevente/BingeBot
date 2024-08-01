package com.vadlevente.bingebot.list.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.list.MovieListViewModel
import com.vadlevente.bingebot.list.R
import kotlinx.coroutines.launch
import com.vadlevente.bingebot.resources.R as Res

@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    ItemListScreen(
        viewModel = viewModel,
        resources = ItemListScreenResources(
            title = R.string.movieList_pageTitle,
            searchFieldHint = Res.string.searchField_movieHint,
            emptyListDescription = Res.string.emptyList_movie_Description,
        )
    )
    BackHandler {
        coroutineScope.launch {
            viewModel.showExitConfirmation()
        }
    }
}