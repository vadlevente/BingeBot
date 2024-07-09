package com.vadlevente.bingebot.core.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.core.asString
import com.vadlevente.bingebot.core.viewModel.DialogViewModel
import com.vadlevente.bingebot.ui.dialogDescription
import com.vadlevente.bingebot.ui.dialogTitle

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
            .background(Color.LightGray)
        ) {
            event.title?.asString()?.let {
                Text(
                    text = it,
                    style = dialogTitle,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            event.content?.asString()?.let {
                Text(
                    text = it,
                    style = dialogDescription,
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
                    ElevatedButton(onClick = viewModel::onNegativeClicked) {
                        Text(text = it)
                    }
                }
                event.positiveButtonTitle?.asString()?.let {
                    ElevatedButton(onClick = viewModel::onPositiveClicked) {
                        Text(text = it)
                    }
                }
            }
        }
    }
}