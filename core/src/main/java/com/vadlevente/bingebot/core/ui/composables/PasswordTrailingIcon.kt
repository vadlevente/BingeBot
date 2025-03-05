package com.vadlevente.bingebot.core.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun PasswordTrailingIcon(
    isPasswordVisible: Boolean,
    onToggleClicked: () -> Unit,
) {
    val image = if (isPasswordVisible)
        Icons.Filled.VisibilityOff
    else Icons.Filled.Visibility

    IconButton(onClick = onToggleClicked) {
        Icon(
            imageVector = image,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}