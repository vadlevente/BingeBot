package com.vadlevente.bingebot.watchlist.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.vadlevente.bingebot.ui.backgroundColor
import com.vadlevente.bingebot.ui.lightTextColor
import com.vadlevente.bingebot.ui.listDescription
import com.vadlevente.bingebot.ui.progressColor
import com.vadlevente.bingebot.ui.white
import com.vadlevente.bingebot.watchlist.WatchListViewModel
import com.vadlevente.bingebot.watchlist.WatchListViewModel.ViewState
import com.vadlevente.bingebot.resources.R as Res

@Composable
fun WatchListScreen(
    watchListId: String,
    viewModel: WatchListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val isInProgress by viewModel.isInProgress.collectAsState()
    LaunchedEffect(true) {
        viewModel.onInit(watchListId)
    }
    WatchListScreenComponent(
        state = state,
        isInProgress = isInProgress,
        onToggleSearchField = viewModel::onToggleSearchField,
        onQueryChanged = viewModel::onQueryChanged,
        onNavigateToOptions = viewModel::onNavigateToOptions,
        onDeleteWatchList = viewModel::onDeleteWatchList,
        onBackPressed = viewModel::onBackPressed,
        onNavigateToSearch = viewModel::onNavigateToSearch,
    )
}

@Composable
fun WatchListScreenComponent(
    state: ViewState,
    isInProgress: Boolean,
    onToggleSearchField: () -> Unit,
    onQueryChanged: (String) -> Unit,
    onNavigateToOptions: (Int) -> Unit,
    onDeleteWatchList: () -> Unit,
    onBackPressed: () -> Unit,
    onNavigateToSearch: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringOf(state.title ?: ""),
                canNavigateBack = true,
                onBackPressed = onBackPressed,
                actions = {
                    Icon(
                        imageVector = Filled.Search,
                        contentDescription = null,
                        tint = lightTextColor,
                        modifier = Modifier
                            .clickable { onToggleSearchField() }
                            .padding(end = 8.dp)
                    )
                    Icon(
                        imageVector = Filled.Delete,
                        contentDescription = null,
                        tint = lightTextColor,
                        modifier = Modifier
                            .clickable { onDeleteWatchList() }
                            .padding(end = 8.dp)
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToSearch,
                containerColor = progressColor,
                contentColor = white,
            ) {
                Icon(Filled.Add, "")
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
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    value = state.searchQuery ?: "",
                    hint = stringOf(Res.string.searchField_movieHint),
                    onValueChange = onQueryChanged,
                )
            }
            ProgressScreen(
                isProgressVisible = isInProgress,
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.movies.isEmpty()) {
                    val descriptionStringRes =
                        if (state.searchQuery == null) Res.string.emptyList_movie_Description
                        else Res.string.emptyQueriedListDescription
                    Text(
                        text = stringResource(descriptionStringRes),
                        style = listDescription,
                        modifier = Modifier.align(Alignment.Center),
                    )
                } else {
                    LazyColumn {
                        items(state.movies) { displayedMovie ->
                            val movie = displayedMovie.item
                            ListItem(
                                title = movie.title,
                                iconPath = displayedMovie.thumbnailUrl,
                                watchedDate = movie.watchedDate,
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