package com.vadlevente.bingebot.bottomsheet.ui.tv

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.bottomsheet.ui.OrderByBottomSheet
import com.vadlevente.bingebot.bottomsheet.viewmodel.tv.TvOrderByBottomSheetViewModel

@Composable
fun TvOrderByBottomSheet(
    viewModel: TvOrderByBottomSheetViewModel = hiltViewModel(),
) {
    OrderByBottomSheet(
        viewModel = viewModel,
    )
}