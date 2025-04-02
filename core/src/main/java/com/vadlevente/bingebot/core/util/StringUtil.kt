package com.vadlevente.bingebot.core.util

import java.text.NumberFormat
import java.util.Currency

val Float.asOneDecimalString
    get() = "%.1f".format(this)

val Long.asDollarAmount: String
    get() {
        val format = NumberFormat.getNumberInstance().apply {
            currency = Currency.getInstance("USD")
        }
        return "$${format.format(this)}"
    }