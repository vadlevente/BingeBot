package com.vadlevente.bingebot.core.util

val Float.asOneDecimalString
    get() = "%.1f".format(this)