package com.vadlevente.bingebot.bottomsheet.ui

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.AddToPhotos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vadlevente.bingebot.bottomsheet.R
import com.vadlevente.bingebot.bottomsheet.ui.composables.ItemBottomSheetHeader
import com.vadlevente.bingebot.bottomsheet.viewmodel.ItemBottomSheetViewModel
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.ui.composables.BBButton
import com.vadlevente.bingebot.core.ui.composables.BBOutlinedButton
import com.vadlevente.bingebot.core.util.isBeforeTomorrow
import com.vadlevente.bingebot.core.util.yearString
import com.vadlevente.bingebot.ui.BingeBotTheme
import com.vadlevente.bingebot.ui.bottomSheetBottomPadding
import java.util.Calendar
import java.util.Date
import com.vadlevente.bingebot.resources.R as Resources

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Item> ItemBottomSheet(
    viewModel: ItemBottomSheetViewModel<T>,
    resources: ItemBottomSheetResources,
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val state by viewModel.state.collectAsState()
    val event = state.event ?: return
    val displayedItem = event.item
    val item = displayedItem.item
    if (!state.isVisible) return
    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = Date().time,
        selectableDates = object: SelectableDates {
            override fun isSelectableYear(year: Int): Boolean {
                return year >= Calendar.getInstance().get(Calendar.YEAR)
            }

            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis.isBeforeTomorrow
            }
        }
    )
    var showDialog by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = viewModel::onDismiss,
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            ItemBottomSheetHeader(
                thumbnailUrl = displayedItem.thumbnailUrl,
                title = item.title,
                releaseYear = item.releaseDate?.yearString ?: "",
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                if (!event.openedFromDetail) {
                    BottomSheetAction(
                        action = { viewModel.onShowDetails() },
                        labelRes = R.string.itemBottomSheet_openDetails,
                        imageVector = Filled.Info,
                    )
                }
                if (event.watchListId == null) {
                    BottomSheetAction(
                        action = { viewModel.onAddToWatchList() },
                        labelRes = R.string.itemBottomSheet_addToWatchList,
                        imageVector = Filled.AddToPhotos,
                    )
                } else {
                    BottomSheetAction(
                        action = { viewModel.removeFromWatchList() },
                        labelRes = R.string.itemBottomSheet_removeFromWatchList,
                        imageVector = Filled.Remove,
                    )
                }
                if (event.alreadySaved) {
                    if (item.isWatched) {
                        BottomSheetAction(
                            action = { viewModel.onSetItemNotWatched() },
                            labelRes = R.string.itemBottomSheet_revertSeen,
                            imageVector = Filled.VisibilityOff,
                        )
                    } else {
                        BottomSheetAction(
                            action = { showDialog = true },
                            labelRes = R.string.itemBottomSheet_seen,
                            imageVector = Filled.Visibility,
                        )
                    }
                    BottomSheetAction(
                        action = { viewModel.onDelete() },
                        labelRes = R.string.itemBottomSheet_delete,
                        imageVector = Filled.Delete,
                    )
                } else {
                    BottomSheetAction(
                        action = { viewModel.onSaveItem(event.item.item) },
                        labelRes = resources.deleteTitle,
                        imageVector = Filled.SaveAlt,
                    )
                }
                Spacer(modifier = Modifier.height(bottomSheetBottomPadding))
            }
        }
    }
    BackHandler {
        viewModel.onDismiss()
    }
    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                BBButton(
                    modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
                    onClick = {
                        showDialog = false
                        dateState.selectedDateMillis?.let {
                            viewModel.onSetItemWatched(it)
                        }
                    },
                    text = stringResource(id = Resources.string.common_Ok)
                )
            },
            dismissButton = {
                BBOutlinedButton(
                    modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
                    text = stringResource(id = Resources.string.common_Cancel),
                    onClick = { showDialog = false }
                )
            },
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface,
            )
        ) {
            DatePicker(
                title = {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = stringResource(id = R.string.itemBottomSheet_addWatchedDateTitle),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                },
                state = dateState,
                showModeToggle = false,
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    headlineContentColor = MaterialTheme.colorScheme.primary,
                    weekdayContentColor = MaterialTheme.colorScheme.primary,
                    yearContentColor = MaterialTheme.colorScheme.primary,
                    currentYearContentColor = MaterialTheme.colorScheme.primary,
                    selectedYearContentColor = MaterialTheme.colorScheme.primary,
                    dayContentColor = MaterialTheme.colorScheme.primary,
                    selectedDayContentColor = MaterialTheme.colorScheme.onSurface,
                    todayContentColor = MaterialTheme.colorScheme.primary,
                    todayDateBorderColor = BingeBotTheme.colors.highlight,
                    selectedDayContainerColor = BingeBotTheme.colors.highlight,
                    subheadContentColor = BingeBotTheme.colors.highlight,
                ),
            )
        }
    }
}

@Composable
private fun BottomSheetAction(
    action: () -> Unit,
    @StringRes labelRes: Int,
    imageVector: ImageVector,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { action() }
            .padding(horizontal = 8.dp)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = imageVector,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null,
        )
        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = stringResource(id = labelRes),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

data class ItemBottomSheetResources(
    val deleteTitle: Int
)