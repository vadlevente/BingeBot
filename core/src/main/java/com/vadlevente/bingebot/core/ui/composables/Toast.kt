package com.vadlevente.bingebot.core.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vadlevente.bingebot.core.asString
import com.vadlevente.bingebot.core.events.toast.ToastType.ERROR
import com.vadlevente.bingebot.core.events.toast.ToastType.INFO
import com.vadlevente.bingebot.core.events.toast.ToastType.WARNING
import com.vadlevente.bingebot.core.viewModel.ToastViewModel
import com.vadlevente.bingebot.ui.BingeBotTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun Toast(
    modifier: Modifier,
    toastViewModel: ToastViewModel = hiltViewModel(),
) {
    val viewState by toastViewModel.state.collectAsStateWithLifecycle()
    var ticks by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    LaunchedEffect(viewState.isVisible) {
        if (!viewState.isVisible) return@LaunchedEffect
        while (ticks < TOAST_LENGTH) {
            delay(1.seconds)
            ticks++
        }
        ticks = 0
        toastViewModel.onHideToast()
    }
    AnimatedVisibility(
        visible = viewState.isVisible,
        enter = slideInVertically {
            with(density) { -40.dp.roundToPx() }
        },
        exit = slideOutVertically(),
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    when (viewState.type) {
                        INFO -> BingeBotTheme.colors.info
                        WARNING -> BingeBotTheme.colors.warning
                        ERROR -> MaterialTheme.colorScheme.error
                    }
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = viewState.text.asString(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

private const val TOAST_LENGTH = 4