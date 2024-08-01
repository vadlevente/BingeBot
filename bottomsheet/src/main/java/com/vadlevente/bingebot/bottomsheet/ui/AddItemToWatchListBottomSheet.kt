package com.vadlevente.bingebot.bottomsheet.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vadlevente.bingebot.bottomsheet.R
import com.vadlevente.bingebot.bottomsheet.ui.composables.ItemBottomSheetHeader
import com.vadlevente.bingebot.bottomsheet.viewmodel.AddItemToWatchListBottomSheetViewModel
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.ui.composables.ProgressScreen
import com.vadlevente.bingebot.core.util.yearString
import com.vadlevente.bingebot.ui.bottomSheetAction
import com.vadlevente.bingebot.ui.cardColor
import com.vadlevente.bingebot.ui.lightTextColor
import com.vadlevente.bingebot.ui.onBackgroundColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Item> AddItemToWatchListBottomSheet(
    viewModel: AddItemToWatchListBottomSheetViewModel<T>,
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val state by viewModel.state.collectAsState()
    val isInProgress by viewModel.isInProgress.collectAsState()
    val displayedItem = state.event?.item ?: return
    val movie = displayedItem.item
    if (!state.isVisible) return

    ModalBottomSheet(
        onDismissRequest = viewModel::onDismiss,
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = cardColor,
    ) {
        Column(
            modifier = Modifier
            .fillMaxWidth()
                .background(cardColor)
        ) {
            ItemBottomSheetHeader(
                thumbnailUrl = displayedItem.thumbnailUrl,
                title = movie.title,
                releaseYear = movie.releaseDate?.yearString ?: "",
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(onBackgroundColor)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.onCreateWatchList() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = lightTextColor,
                )
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = stringResource(id = R.string.addItemToWatchListBottomSheet_createWatchListLabel),
                    style = bottomSheetAction,
                )
            }
            ProgressScreen(
                isProgressVisible = isInProgress,
                modifier = Modifier.fillMaxWidth(),
            ) {
                LazyColumn {
                    items(
                        items = state.watchLists,
                        key = { it.watchListId },
                    ) { watchList ->
                        Text(
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .padding(horizontal = 16.dp)
                                .clickable {
                                    viewModel.onAddToWatchList(watchList.watchListId)
                                },
                            text = watchList.title,
                            style = bottomSheetAction,
                        )
                    }
                }
            }
        }
    }
    BackHandler {
        viewModel.onDismiss()
    }
}

