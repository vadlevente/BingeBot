package com.vadlevente.bingebot.bottomsheet.ui.movie

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.bottomsheet.R.string
import com.vadlevente.bingebot.bottomsheet.ui.ItemBottomSheet
import com.vadlevente.bingebot.bottomsheet.ui.ItemBottomSheetResources
import com.vadlevente.bingebot.bottomsheet.viewmodel.movie.MovieBottomSheetViewModel

@Composable
fun MovieBottomSheet(
    viewModel: MovieBottomSheetViewModel = hiltViewModel(),
) {
    ItemBottomSheet(
        viewModel = viewModel,
        resources = ItemBottomSheetResources(
            deleteTitle = string.movieBottomSheet_save,
        )
    )
}