package com.vadlevente.bingebot.bottomsheet.ui.tv

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.bottomsheet.R.string
import com.vadlevente.bingebot.bottomsheet.ui.ItemWatchListsBottomSheet
import com.vadlevente.bingebot.bottomsheet.ui.ItemWatchListsBottomSheetResources
import com.vadlevente.bingebot.bottomsheet.viewmodel.tv.TvWatchListsBottomSheetViewModel

@Composable
fun TvWatchListsBottomSheet(
    viewModel: TvWatchListsBottomSheetViewModel = hiltViewModel(),
) {
    ItemWatchListsBottomSheet(
        viewModel = viewModel,
        resources = ItemWatchListsBottomSheetResources(
            title = string.tvWatchListBottomSheet_title,
        ),
    )
}

