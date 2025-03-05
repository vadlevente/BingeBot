package com.vadlevente.bingebot.core.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vadlevente.bingebot.ui.BingeBotTheme

@Composable
fun ProgressScreen(
    modifier: Modifier = Modifier,
    isProgressVisible: Boolean,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier) {
        if (isProgressVisible) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(12.dp),
                color = BingeBotTheme.colors.highlight,
            )
        } else {
            content()
        }
    }
}