package com.vadlevente.bingebot.core.ui.composables.bottomsheet

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.core.R.string
import com.vadlevente.bingebot.core.util.yearString
import com.vadlevente.bingebot.core.viewModel.bottomsheet.MovieBottomSheetViewModel
import com.vadlevente.bingebot.ui.bottomSheetAction
import com.vadlevente.bingebot.ui.cardColor
import com.vadlevente.bingebot.ui.lightTextColor
import com.vadlevente.bingebot.ui.onBackgroundColor

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
                    labelRes = string.movieBottomSheet_openDetails,
                    imageVector = Filled.Info,
                )
                if (event.watchListId == null) {
                    BottomSheetAction(
                        action = { viewModel.onAddToWatchList() },
                        labelRes = string.movieBottomSheet_addToWatchList,
                        imageVector = Filled.AddToPhotos,
                    )
                } else {
                    BottomSheetAction(
                        action = { viewModel.removeFromWatchList() },
                        labelRes = string.movieBottomSheet_removeFromWatchList,
                        imageVector = Filled.Remove,
                    )
                }
                if (event.alreadySaved) {
                    if (movie.isWatched) {
                        BottomSheetAction(
                            action = { viewModel.onSetMovieNotWatched() },
                            labelRes = string.movieBottomSheet_revertSeen,
                            imageVector = Filled.VisibilityOff,
                        )
                    } else {
                        BottomSheetAction(
                            action = { viewModel.onSetMovieWatched() },
                            labelRes = string.movieBottomSheet_seen,
                            imageVector = Filled.Visibility,
                        )
                    }
                    BottomSheetAction(
                        action = { viewModel.onDelete() },
                        labelRes = string.movieBottomSheet_delete,
                        imageVector = Filled.Delete,
                    )
                } else {
                    BottomSheetAction(
                        action = { viewModel.onSaveMovie(event.movie.movie) },
                        labelRes = string.movieBottomSheet_save,
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