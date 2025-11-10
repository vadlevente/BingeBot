package com.vadlevente.bingebot.common.ui.util

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.vadlevente.bingebot.core.ui.UIText
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

@Composable
fun ImagePlaceholder(color: Color = MaterialTheme.colorScheme.background) =
    object: Painter() {
        override val intrinsicSize: Size
            get() = Size(100f, 100f)

        override fun DrawScope.onDraw() {
            drawRect(color)
        }
    }

@Composable
fun UIText.asString() = asString(LocalContext.current)

@Composable
fun Collection<UIText?>.asString() = asString(LocalContext.current)

@Suppress("SpreadOperator")
fun UIText.asString(context: Context): String =
    when (this) {
        is UIText.DynamicText -> text
        is UIText.StringResource -> context.getString(resId, *(args.formatText(context)))
        is UIText.CombinedText -> parts.joinToString(separator = "") { it.asString(context) }

    }

fun Collection<UIText?>.asString(context: Context) = filterNotNull().map { it.asString(context) }

private fun Collection<Any>.formatText(context: Context) =
    map { (it as? UIText)?.asString(context) ?: it }.toTypedArray()