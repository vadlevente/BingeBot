package com.vadlevente.bingebot.core.ui.composables

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddToPhotos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Visibility
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.vadlevente.bingebot.core.R
import com.vadlevente.bingebot.core.R.drawable
import com.vadlevente.bingebot.core.viewModel.BottomSheetViewModel
import com.vadlevente.bingebot.ui.bottomSheetAction
import com.vadlevente.bingebot.ui.listItemSubtitle
import com.vadlevente.bingebot.ui.listItemTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieBottomSheet(
    viewModel: BottomSheetViewModel = hiltViewModel(),
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val state by viewModel.state.collectAsState()
    val event = state.event ?: return
    if (!state.isVisible) return

    ModalBottomSheet(
        onDismissRequest = viewModel::onDismiss,
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(
                    model = event.thumbnailUrl,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .width(60.dp),
                    placeholder = painterResource(id = drawable.movie_poster_placeholder),
                    error = painterResource(id = drawable.movie_poster_placeholder),
                    contentDescription = null,
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = event.title,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        style = listItemTitle,
                    )
                    Text(
                        text = event.releaseYear,
                        style = listItemSubtitle,
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.DarkGray)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                BottomSheetAction(
                    action = { viewModel.onAddToWatchList() },
                    labelRes = R.string.movieBottomSheet_addToWatchList,
                    imageVector = Icons.Filled.AddToPhotos,
                )
                BottomSheetAction(
                    action = { viewModel.onDelete() },
                    labelRes = R.string.movieBottomSheet_delete,
                    imageVector = Icons.Filled.Delete,
                )
                BottomSheetAction(
                    action = { viewModel.onShowDetails() },
                    labelRes = R.string.movieBottomSheet_openDetails,
                    imageVector = Icons.Filled.Visibility,
                )
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
            contentDescription = null,
        )
        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = stringResource(id = labelRes),
            style = bottomSheetAction,
        )
    }
}