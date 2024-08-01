package com.vadlevente.bingebot.core.model

enum class SelectedLanguage(
    val code: String,
    val displayName: String,
) {
    HUNGARIAN("hu", "Magyar"),
    ENGLISH("en", "English");
    companion object {
        fun from(code: String) = values().first { it.code == code }
        val default = HUNGARIAN
    }
}