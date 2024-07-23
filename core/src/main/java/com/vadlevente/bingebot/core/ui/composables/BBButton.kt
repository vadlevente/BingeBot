package com.vadlevente.bingebot.core.ui.composables

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vadlevente.bingebot.ui.buttonLabel
import com.vadlevente.bingebot.ui.progressColor

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
            containerColor = progressColor,
            contentColor = progressColor,
        )
    ) {
        Text(
            text = text,
            style = buttonLabel,
        )
    }
}