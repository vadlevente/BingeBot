package com.vadlevente.bingebot.bottomsheet.ui.movie

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.bottomsheet.ui.OrderByBottomSheet
import com.vadlevente.bingebot.bottomsheet.viewmodel.movie.MovieOrderByBottomSheetViewModel

@Composable
fun MovieOrderByBottomSheet(
    viewModel: MovieOrderByBottomSheetViewModel = hiltViewModel(),
) {
    OrderByBottomSheet(
        viewModel = viewModel,
    )
}