package com.vadlevente.bingebot.list.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.ui.composables.BBOutlinedTextField
import com.vadlevente.bingebot.core.ui.composables.ListItem
import com.vadlevente.bingebot.core.ui.composables.ProgressScreen
import com.vadlevente.bingebot.core.ui.composables.TopBar
import com.vadlevente.bingebot.core.util.asOneDecimalString
import com.vadlevente.bingebot.core.util.yearString
import com.vadlevente.bingebot.list.MovieListViewModel
import com.vadlevente.bingebot.list.MovieListViewModel.ViewState
import com.vadlevente.bingebot.list.R
import com.vadlevente.bingebot.ui.listDescription

@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val isInProgress by viewModel.isInProgress.collectAsState()
    MovieListScreenComponent(
        state = state,
        isInProgress = isInProgress,
        onNavigateToSearch = viewModel::onNavigateToSearch,
        onNavigateToDetails = viewModel::onNavigateToDetails,
        onToggleSearchField = viewModel::onToggleSearchField,
        onQueryChanged = viewModel::onQueryChanged,
    )
}

@Composable
fun MovieListScreenComponent(
    state: ViewState,
    isInProgress: Boolean,
    onNavigateToSearch: () -> Unit,
    onNavigateToDetails: (Int) -> Unit,
    onToggleSearchField: () -> Unit,
    onQueryChanged: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringOf(R.string.movieList_pageTitle),
                actions = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable { onToggleSearchField() }
                            .padding(end = 8.dp)
                    )
                }
            )
        },
        bottomBar = {},
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToSearch) {
                Icon(Icons.Filled.Add, "")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            AnimatedVisibility(visible = state.isSearchFieldVisible) {
                BBOutlinedTextField(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    value = state.searchQuery ?: "",
                    hint = stringOf(R.string.movieList_searchFieldHint),
                    onValueChange = onQueryChanged,
                )
            }
            ProgressScreen(
                isProgressVisible = isInProgress,
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.movies.isEmpty()) {
                    val descriptionStringRes =
                        if (state.searchQuery == null) R.string.movieList_emptyListDescription
                        else R.string.movieList_emptyQueriedListDescription
                    Text(
                        text = stringResource(descriptionStringRes),
                        style = listDescription,
                        modifier = Modifier.align(Alignment.Center),
                    )
                } else {
                    LazyColumn {
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

        }
    }

}