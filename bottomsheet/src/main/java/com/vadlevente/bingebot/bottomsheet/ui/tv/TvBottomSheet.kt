package com.vadlevente.bingebot.bottomsheet.ui.tv

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.bottomsheet.R.string
import com.vadlevente.bingebot.bottomsheet.ui.ItemBottomSheet
import com.vadlevente.bingebot.bottomsheet.ui.ItemBottomSheetResources
import com.vadlevente.bingebot.bottomsheet.viewmodel.tv.TvBottomSheetViewModel

@Composable
fun TvBottomSheet(
    viewModel: TvBottomSheetViewModel = hiltViewModel(),
) {
    ItemBottomSheet(
        viewModel = viewModel,
        resources = ItemBottomSheetResources(
            deleteTitle = string.tvBottomSheet_save,
        )
    )
}