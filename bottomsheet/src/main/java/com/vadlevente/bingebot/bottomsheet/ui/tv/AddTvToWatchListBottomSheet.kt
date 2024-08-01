package com.vadlevente.bingebot.bottomsheet.ui.tv

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.bottomsheet.ui.AddItemToWatchListBottomSheet
import com.vadlevente.bingebot.bottomsheet.viewmodel.tv.AddTvToWatchListBottomSheetViewModel

@Composable
fun AddTvToWatchListBottomSheet(
    viewModel: AddTvToWatchListBottomSheetViewModel = hiltViewModel(),
) {
    AddItemToWatchListBottomSheet(viewModel = viewModel)
}

