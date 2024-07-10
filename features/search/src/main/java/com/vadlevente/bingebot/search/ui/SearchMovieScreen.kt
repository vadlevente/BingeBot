package com.vadlevente.bingebot.search.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.vadlevente.bingebot.core.ui.composables.TopBar
import com.vadlevente.bingebot.core.util.Constants.QUERY_MINIMUM_LENGTH
import com.vadlevente.bingebot.core.util.asOneDecimalString
import com.vadlevente.bingebot.core.util.yearString
import com.vadlevente.bingebot.search.R
import com.vadlevente.bingebot.search.SearchMovieViewModel
import com.vadlevente.bingebot.search.SearchMovieViewModel.ViewState
import com.vadlevente.bingebot.ui.listDescription

@Composable
fun SearchMovieScreen(
    viewModel: SearchMovieViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    SearchMovieScreenComponent(
        state = state,
        onQueryChanged = viewModel::onQueryChanged,
        onNavigateToOptions = viewModel::onNavigateToOptions,
        onBackPressed = viewModel::onBackPressed,
    )
}

@Composable
fun SearchMovieScreenComponent(
    state: ViewState,
    onQueryChanged: (String) -> Unit,
    onNavigateToOptions: (Int) -> Unit,
    onBackPressed: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringOf(R.string.searchMovies_pageTitle),
                canNavigateBack = true,
                onBackPressed = onBackPressed,
            )
        },
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {
            BBOutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                value = state.query,
                hint = stringOf(R.string.searchMovies_searchFieldHint),
                onValueChange = onQueryChanged,
            )
            Box(modifier = Modifier.fillMaxSize()) {
                if (state.query.length < QUERY_MINIMUM_LENGTH) {
                    Text(
                        text = stringResource(R.string.searchMovies_shortQueryDescription),
                        style = listDescription,
                        modifier = Modifier.align(Alignment.Center),
                    )
                } else if (state.movies.isEmpty()) {
                    Text(
                        text = stringResource(R.string.searchMovies_emptyListDescription),
                        style = listDescription,
                        modifier = Modifier.align(Alignment.Center),
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        items(
                            items = state.movies,
                            key = { it.movie.id }
                        ) { displayedMovie ->
                            val movie = displayedMovie.movie
                            ListItem(
                                title = movie.title,
                                iconPath = displayedMovie.backdropUrl,
                                isWatched = movie.watchedDate != null,
                                rating = movie.voteAverage.asOneDecimalString,
                                releaseYear = movie.releaseDate?.yearString ?: "",
                                onClick = { onNavigateToOptions(movie.id) },
                            )
                        }
                    }
                }
            }
        }
    }
}