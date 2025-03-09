package com.vadlevente.bingebot.core.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.vadlevente.bingebot.ui.BingeBotTheme

@Composable
fun BBIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    color: Color = MaterialTheme.colorScheme.primary,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .clickable(enabled = onClick != null) {
                onClick?.invoke()
            }
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = color,
        )
    }
}

@Composable
@Preview
private fun BBIconPreview() {
    BingeBotTheme {
        BBIcon(
            imageVector = Icons.Filled.MoreVert,
            onClick = {}
        )
    }
}