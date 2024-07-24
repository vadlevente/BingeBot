package com.vadlevente.bingebot.bottomsheet.ui

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.background
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
import androidx.compose.material3.ModalBottomSheet
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.bottomsheet.R
import com.vadlevente.bingebot.bottomsheet.viewmodel.MovieBottomSheetViewModel
import com.vadlevente.bingebot.core.ui.composables.BBButton
import com.vadlevente.bingebot.core.ui.composables.BBOutlinedButton
import com.vadlevente.bingebot.core.util.isBeforeTomorrow
import com.vadlevente.bingebot.core.util.yearString
import com.vadlevente.bingebot.ui.bottomSheetAction
import com.vadlevente.bingebot.ui.cardColor
import com.vadlevente.bingebot.ui.darkTextColor
import com.vadlevente.bingebot.ui.dialogDescription
import com.vadlevente.bingebot.ui.lightTextColor
import com.vadlevente.bingebot.ui.onBackgroundColor
import com.vadlevente.bingebot.ui.progressColor
import java.util.Date
import com.vadlevente.bingebot.resources.R as Resources

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieBottomSheet(
    viewModel: MovieBottomSheetViewModel = hiltViewModel(),
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val state by viewModel.state.collectAsState()
    val event = state.event ?: return
    val displayedMovie = event.movie
    val movie = displayedMovie.movie
    if (!state.isVisible) return
    val dateState = rememberDatePickerState(initialSelectedDateMillis = Date().time)
    var showDialog by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = viewModel::onDismiss,
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = cardColor,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            MovieBottomSheetHeader(
                thumbnailUrl = displayedMovie.backdropUrl,
                title = movie.title,
                releaseYear = movie.releaseDate?.yearString ?: "",
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(onBackgroundColor)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                BottomSheetAction(
                    action = { viewModel.onShowDetails() },
                    labelRes = R.string.movieBottomSheet_openDetails,
                    imageVector = Filled.Info,
                )
                if (event.watchListId == null) {
                    BottomSheetAction(
                        action = { viewModel.onAddToWatchList() },
                        labelRes = R.string.movieBottomSheet_addToWatchList,
                        imageVector = Filled.AddToPhotos,
                    )
                } else {
                    BottomSheetAction(
                        action = { viewModel.removeFromWatchList() },
                        labelRes = R.string.movieBottomSheet_removeFromWatchList,
                        imageVector = Filled.Remove,
                    )
                }
                if (event.alreadySaved) {
                    if (movie.isWatched) {
                        BottomSheetAction(
                            action = { viewModel.onSetMovieNotWatched() },
                            labelRes = R.string.movieBottomSheet_revertSeen,
                            imageVector = Filled.VisibilityOff,
                        )
                    } else {
                        BottomSheetAction(
                            action = { showDialog = true },
                            labelRes = R.string.movieBottomSheet_seen,
                            imageVector = Filled.Visibility,
                        )
                    }
                    BottomSheetAction(
                        action = { viewModel.onDelete() },
                        labelRes = R.string.movieBottomSheet_delete,
                        imageVector = Filled.Delete,
                    )
                } else {
                    BottomSheetAction(
                        action = { viewModel.onSaveMovie(event.movie.movie) },
                        labelRes = R.string.movieBottomSheet_save,
                        imageVector = Filled.SaveAlt,
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
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
                            viewModel.onSetMovieWatched(it)
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
                containerColor = cardColor,
            )
        ) {
            DatePicker(
                title = {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = stringResource(id = R.string.movieBottomSheet_addWatchedDateTitle),
                            style = dialogDescription
                        )
                },
                state = dateState,
                showModeToggle = false,
                colors = DatePickerDefaults.colors(
                    containerColor = cardColor,
                    titleContentColor = lightTextColor,
                    headlineContentColor = lightTextColor,
                    weekdayContentColor = lightTextColor,
                    yearContentColor = lightTextColor,
                    currentYearContentColor = lightTextColor,
                    selectedYearContentColor = lightTextColor,
                    dayContentColor = lightTextColor,
                    selectedDayContentColor = darkTextColor,
                    todayContentColor = lightTextColor,
                    todayDateBorderColor = progressColor,
                    selectedDayContainerColor = progressColor,
                    subheadContentColor = progressColor,
                    ),
                dateValidator = {
                    it.isBeforeTomorrow
                }
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
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = imageVector,
            tint = lightTextColor,
            contentDescription = null,
        )
        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = stringResource(id = labelRes),
            style = bottomSheetAction,
        )
    }
}