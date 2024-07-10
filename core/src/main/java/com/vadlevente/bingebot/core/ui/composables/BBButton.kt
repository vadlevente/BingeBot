package com.vadlevente.bingebot.core.ui.composables

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vadlevente.bingebot.ui.buttonLabel
import com.vadlevente.bingebot.ui.onBackgroundColorFocused

@Composable
fun BBButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    ElevatedButton(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = onBackgroundColorFocused,
            contentColor = onBackgroundColorFocused,
        )
    ) {
        Text(
            text = text,
            style = buttonLabel,
        )
    }
}