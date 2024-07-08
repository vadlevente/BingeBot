package com.vadlevente.bingebot.list.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.ui.composables.ListItem
import com.vadlevente.bingebot.core.ui.composables.TopBar
import com.vadlevente.bingebot.core.util.asOneDecimalString
import com.vadlevente.bingebot.core.util.yearString
import com.vadlevente.bingebot.list.MovieListViewModel
import com.vadlevente.bingebot.list.MovieListViewModel.ViewState
import com.vadlevente.bingebot.list.R

@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    MovieListScreenComponent(
        state = state,
        onNavigateToSearch = viewModel::onNavigateToSearch,
        onNavigateToDetails = viewModel::onNavigateToDetails,
    )
}

@Composable
fun MovieListScreenComponent(
    state: ViewState,
    onNavigateToSearch: () -> Unit,
    onNavigateToDetails: (Int) -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(title = stringOf(R.string.movieList_pageTitle))
        },
        bottomBar = {},
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToSearch) {
                Icon(Icons.Filled.Add,"")
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(state.movies) { displayedMovie ->
                val movie = displayedMovie.movie
                ListItem(
                    title = movie.title,
                    iconPath = displayedMovie.backdropUrl,
                    isWatched = movie.watchedDate != null,
                    rating = movie.voteAverage.asOneDecimalString,
                    releaseYear = movie.releaseDate?.yearString ?: "",
                    onClick = { onNavigateToDetails(movie.id) },
                    onDelete = { /*TODO*/ }) {

                }
            }
        }
    }

}