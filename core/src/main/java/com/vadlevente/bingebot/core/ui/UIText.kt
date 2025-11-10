package com.vadlevente.bingebot.core.ui

import androidx.annotation.StringRes
import kotlin.collections.toList
import kotlin.let

sealed interface UIText {
    data class DynamicText(val text: String) : UIText

    data class StringResource(
        @StringRes val resId: Int,
        val args: List<Any>,
    ) : UIText

    data class CombinedText(val parts: List<UIText>) : UIText

    companion object {
        fun empty() = DynamicText("")
    }
}

fun stringOf(@StringRes resId: Int, vararg args: Any) =
    UIText.StringResource(resId, args.toList())

fun stringOf(@StringRes resId: Int?, vararg args: Any) =
    resId?.let { UIText.StringResource(resId, args.toList()) }

@JvmName("stringOf")
fun stringOf(text: String) = UIText.DynamicText(text)

@JvmName("nullableStringOf")
fun stringOf(text: String?) = text?.let { UIText.DynamicText(text) }

fun stringOf(vararg text: UIText) = UIText.CombinedText(text.toList())
