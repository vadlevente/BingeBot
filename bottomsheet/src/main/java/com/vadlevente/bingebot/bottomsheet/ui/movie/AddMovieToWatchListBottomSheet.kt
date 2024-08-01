package com.vadlevente.bingebot.bottomsheet.ui.movie

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.bottomsheet.ui.AddItemToWatchListBottomSheet
import com.vadlevente.bingebot.bottomsheet.viewmodel.movie.AddMovieToWatchListBottomSheetViewModel

@Composable
fun AddMovieToWatchListBottomSheet(
    viewModel: AddMovieToWatchListBottomSheetViewModel = hiltViewModel(),
) {
    AddItemToWatchListBottomSheet(viewModel = viewModel)
}

