package com.vadlevente.bingebot.core

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.vadlevente.bingebot.core.UIText.DynamicText
import com.vadlevente.bingebot.core.UIText.StringResource

sealed interface UIText {
    data class DynamicText(val text: String) : UIText

    data class StringResource(
        @StringRes val resId: Int,
        val args: List<Any>,
    ) : UIText

    companion object {
        fun empty() = DynamicText("")
    }
}

@Composable
fun UIText.asString() = asString(LocalContext.current)

@Suppress("SpreadOperator")
fun UIText.asString(context: Context): String =
    when (this) {
        is DynamicText -> text
        is StringResource -> context.getString(resId, *(args.formatText(context)))
    }

@Composable
fun Collection<UIText?>.asString() = asString(LocalContext.current)
fun Collection<UIText?>.asString(context: Context) = filterNotNull().map { it.asString(context) }

private fun Collection<Any>.formatText(context: Context) =
    map { (it as? UIText)?.asString(context) ?: it }.toTypedArray()

fun stringOf(@StringRes resId: Int, vararg args: Any) =
    StringResource(resId, args.toList())

fun stringOf(@StringRes resId: Int?, vararg args: Any) =
    resId?.let { StringResource(resId, args.toList()) }

@JvmName("stringOf")
fun stringOf(text: String) = DynamicText(text)

@JvmName("nullableStringOf")
fun stringOf(text: String?) = text?.let { DynamicText(text) }
