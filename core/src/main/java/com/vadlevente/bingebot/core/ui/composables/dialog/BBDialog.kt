package com.vadlevente.bingebot.core.ui.composables.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.core.asString
import com.vadlevente.bingebot.core.ui.composables.BBButton
import com.vadlevente.bingebot.core.ui.composables.BBOutlinedButton
import com.vadlevente.bingebot.core.viewModel.dialog.DialogViewModel

@Composable
fun BBDialog(
    viewModel: DialogViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val event = state.event ?: return
    if (!state.isVisible) return
    Dialog(
        onDismissRequest = viewModel::onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = event.isCancelable,
            dismissOnClickOutside = event.isCancelable
        )
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp)
        ) {
            event.title?.asString()?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            event.content?.asString()?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                event.negativeButtonTitle?.asString()?.let {
                    BBOutlinedButton(
                        text = it,
                        onClick = viewModel::onNegativeClicked
                    )
                }
                event.positiveButtonTitle?.asString()?.let {
                    BBButton(
                        text = it,
                        onClick = viewModel::onPositiveClicked,
                    )
                }
            }
        }
    }
}