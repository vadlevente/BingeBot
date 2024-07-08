package com.vadlevente.bingebot.core.model

enum class SelectedLanguage(val code: String) {
    HUNGARIAN("hu"),
    ENGLISH("en");
    companion object {
        fun from(code: String) = values().first { it.code == code }
        val default = HUNGARIAN
    }
}