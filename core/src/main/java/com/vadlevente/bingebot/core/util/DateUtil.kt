package com.vadlevente.bingebot.core.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

private const val DATE_FORMAT_YEAR = "yyyy"
private const val DATE_FORMAT_FULL = "yyyy.MM.dd"

val Date.yearString: String
    get() = SimpleDateFormat(DATE_FORMAT_YEAR).format(this)

val Date.dateString: String
    get() = SimpleDateFormat(DATE_FORMAT_FULL).format(this)

val Long.isBeforeTomorrow: Boolean
    get() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this
        return calendar.before(Calendar.getInstance())
    }