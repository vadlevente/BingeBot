package com.vadlevente.bingebot.core.ui.composables

import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vadlevente.bingebot.ui.outlinedButtonLabel

@Composable
fun BBOutlinedButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
    ) {
        Text(
            text = text,
            style = outlinedButtonLabel,
        )
    }
}