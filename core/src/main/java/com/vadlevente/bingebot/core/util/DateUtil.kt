package com.vadlevente.bingebot.core.util

import java.text.SimpleDateFormat
import java.util.Date

private const val DATE_FORMAT_YEAR = "yyyy"

val Date.yearString: String
    get() = SimpleDateFormat(DATE_FORMAT_YEAR).format(this)