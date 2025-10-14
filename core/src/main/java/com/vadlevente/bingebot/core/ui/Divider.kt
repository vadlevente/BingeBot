package com.vadlevente.bingebot.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vadlevente.bingebot.ui.DarkGray

@Composable
fun BBDivider(
    modifier: Modifier = Modifier,
    color: Color,
) {
    HorizontalDivider(
        modifier = modifier
            .fillMaxWidth(),
        thickness = 10.dp,
        color = DarkGray //MaterialTheme.colorScheme.surface
    )
}