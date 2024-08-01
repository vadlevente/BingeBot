package com.vadlevente.bingebot.bottomsheet.ui.movie

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.bottomsheet.R
import com.vadlevente.bingebot.bottomsheet.ui.ItemWatchListsBottomSheet
import com.vadlevente.bingebot.bottomsheet.ui.ItemWatchListsBottomSheetResources
import com.vadlevente.bingebot.bottomsheet.viewmodel.movie.MovieWatchListsBottomSheetViewModel

@Composable
fun MovieWatchListsBottomSheet(
    viewModel: MovieWatchListsBottomSheetViewModel = hiltViewModel(),
) {
    ItemWatchListsBottomSheet(
        viewModel = viewModel,
        resources = ItemWatchListsBottomSheetResources(
            title = R.string.movieWatchListBottomSheet_title,
        ),
    )
}

