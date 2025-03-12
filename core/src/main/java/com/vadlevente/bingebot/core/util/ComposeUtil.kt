package com.vadlevente.bingebot.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.vadlevente.bingebot.core.viewModel.LifecycleAwareViewModel

@Composable
fun SetupLifecycle(viewModel: LifecycleAwareViewModel<*>) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> viewModel.onResume(lifecycleOwner)
                Lifecycle.Event.ON_DESTROY -> viewModel.onDestroy(lifecycleOwner)
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}