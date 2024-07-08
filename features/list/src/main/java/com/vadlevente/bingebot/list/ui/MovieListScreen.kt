package com.vadlevente.bingebot.list.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.list.MovieListViewModel
import com.vadlevente.bingebot.list.MovieListViewModel.ViewState

@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    MovieListScreenComponent(
        state = state,
        onNavigateToSearch = viewModel::onNavigateToSearch
    )
}

@Composable
fun MovieListScreenComponent(
    state: ViewState,
    onNavigateToSearch: () -> Unit
) {
    Text(text = "ListScreen")
    FloatingActionButton(onClick = onNavigateToSearch) {

    }
    LazyColumn {
        items(state.movies) {
            Text(text = it.title)
        }
    }
}