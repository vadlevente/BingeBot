package com.vadlevente.bingebot.bottomsheet.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vadlevente.bingebot.bottomsheet.R
import com.vadlevente.bingebot.bottomsheet.viewmodel.OrderByBottomSheetViewModel
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.model.OrderBy
import com.vadlevente.bingebot.ui.BingeBotTheme
import com.vadlevente.bingebot.ui.bottomSheetBottomPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Item> OrderByBottomSheet(
    viewModel: OrderByBottomSheetViewModel<T>
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val state by viewModel.state.collectAsState()
    if (!state.isVisible) return

    ModalBottomSheet(
        onDismissRequest = viewModel::onDismiss,
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                text = stringResource(id = R.string.orderByBottomSheet_title),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(state.orderByList) { index, orderBy ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (!orderBy.isSelected) {
                                    viewModel.onChangeOrderBy(orderBy.orderBy)
                                }
                            }
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(bottom = 16.dp)
                                .padding(horizontal = 16.dp),
                            text = when (orderBy.orderBy) {
                                OrderBy.DATE_ADDED_ASCENDING -> R.string.orderByBottomSheet_date_ascending
                                OrderBy.DATE_ADDED_DESCENDING -> R.string.orderByBottomSheet_date_descending
                                OrderBy.RATING_ASCENDING -> R.string.orderByBottomSheet_rating_ascending
                                OrderBy.RATING_DESCENDING -> R.string.orderByBottomSheet_rating_descending
                            }.let { stringResource(it) },
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        if (orderBy.isSelected) {
                            Icon(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                imageVector = Filled.Done,
                                contentDescription = null,
                                tint = BingeBotTheme.colors.highlight,
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(bottomSheetBottomPadding))
        }
    }
}