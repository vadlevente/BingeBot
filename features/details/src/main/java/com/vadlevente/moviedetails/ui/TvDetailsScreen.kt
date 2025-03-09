package com.vadlevente.moviedetails.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.moviedetails.TvDetailsViewModel

@Composable
fun TvDetailsScreen(
    tvId: Int,
) {
    val viewModel = hiltViewModel<TvDetailsViewModel, TvDetailsViewModel.TvDetailsViewModelFactory> { factory ->
        factory.create(tvId)
    }
    ItemDetailsScreen(
        viewModel = viewModel
    ) {}
}