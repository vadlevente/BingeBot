package com.vadlevente.bingebot.list.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import com.vadlevente.bingebot.core.model.Genre
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
import com.vadlevente.bingebot.list.domain.model.DisplayedGenre
import com.vadlevente.bingebot.ui.backgroundColor
import com.vadlevente.bingebot.ui.darkTextColor
import com.vadlevente.bingebot.ui.infoColor
import com.vadlevente.bingebot.ui.lightTextColor
import com.vadlevente.bingebot.ui.listDescription
import com.vadlevente.bingebot.ui.onBackgroundColor
import com.vadlevente.bingebot.ui.progressColor
import com.vadlevente.bingebot.ui.selectedChipLabel
import com.vadlevente.bingebot.ui.unselectedChipLabel
import com.vadlevente.bingebot.ui.white

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
        onNavigateToOptions = viewModel::onNavigateToOptions,
        onClearGenres = viewModel::onClearGenres,
        onToggleGenre = viewModel::onToggleGenre,
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
    onNavigateToOptions: (Int) -> Unit,
    onClearGenres: () -> Unit,
    onToggleGenre: (Genre) -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringOf(R.string.movieList_pageTitle),
                actions = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null,
                        tint = lightTextColor,
                        modifier = Modifier
                            .clickable { onToggleSearchField() }
                            .padding(end = 8.dp)
                    )
                }
            )
        },
        bottomBar = {},
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToSearch,
                containerColor = infoColor,
                contentColor = white,
            ) {
                Icon(Icons.Filled.Add, "")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .background(backgroundColor)
        ) {
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
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .padding(top = 8.dp)
                    .background(onBackgroundColor)
            )
            GenreSelector(
                state = state,
                onToggleGenre = onToggleGenre,
                onClearGenres = onClearGenres,
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(onBackgroundColor)
                    .padding(bottom = 4.dp)
            )
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
                                onLongClick = { onNavigateToOptions(movie.id) },
                                onDelete = { /*TODO*/ }) {

                            }
                        }
                    }
                }
            }

        }
    }

}

@Composable
private fun GenreSelector(
    state: ViewState,
    onToggleGenre: (Genre) -> Unit,
    onClearGenres: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyRow {
            items(state.genres) { genre ->
                GenreChip(
                    genre = genre,
                    onToggleGenre = onToggleGenre,
                )
            }
        }
        if (state.isAnyGenreSelected) {
            Icon(
                imageVector = Filled.Clear,
                contentDescription = null,
                tint = lightTextColor,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onClearGenres() }
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenreChip(
    genre: DisplayedGenre,
    onToggleGenre: (Genre) -> Unit,
) {
    FilterChip(
        modifier = Modifier.padding(horizontal = 4.dp),
        selected = genre.isSelected,
        onClick = { onToggleGenre(genre.genre) },
        label = {
            Text(
                text = genre.genre.name,
                style = if (genre.isSelected) selectedChipLabel else unselectedChipLabel
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = progressColor,
            labelColor = lightTextColor,
            selectedLabelColor = darkTextColor,
        ),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = onBackgroundColor,
        )
    )
}