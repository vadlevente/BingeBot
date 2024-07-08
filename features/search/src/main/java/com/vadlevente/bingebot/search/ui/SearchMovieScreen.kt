package com.vadlevente.bingebot.search.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.core.model.Movie
import com.vadlevente.bingebot.search.SearchMovieViewModel
import com.vadlevente.bingebot.search.SearchMovieViewModel.ViewState

@Composable
fun SearchMovieScreen(
    viewModel: SearchMovieViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    SearchMovieScreenComponent(
        state = state,
        onQueryChanged = viewModel::onQueryChanged,
        onSaveMovie = viewModel::onSaveMovie,
    )
}

@Composable
fun SearchMovieScreenComponent(
    state: ViewState,
    onQueryChanged: (String) -> Unit,
    onSaveMovie: (Movie) -> Unit,
) {
    TextField(value = state.query, onValueChange = onQueryChanged)
    LazyColumn {
        items(state.movies) {
            Row(modifier = Modifier
                .background(Color.Blue)
                .clickable { onSaveMovie(it) }
            ) {
                Text(text = it.title)
            }
        }
    }
}