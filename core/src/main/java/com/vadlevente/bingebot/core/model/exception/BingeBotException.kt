package com.vadlevente.bingebot.core.model.exception

import com.vadlevente.bingebot.core.R
import com.vadlevente.bingebot.core.UIText
import com.vadlevente.bingebot.core.stringOf

class BingeBotException(
    val reason: Reason? = null,
    val errorMessage: UIText? = null,
    val silent: Boolean = false,
) : Throwable()

enum class Reason(val reasonText: UIText? = null) {
    AUTHENTICATION_FAILED(stringOf(R.string.exception_authenticationFailed)),
    DATA_READ_ERROR,
    DATA_WRITE_ERROR,
    WEAK_PASSWORD(stringOf(R.string.exception_weakPassword)),
    SESSION_EXPIRED(stringOf(R.string.exception_sessionExpired)),
}

fun Throwable.isBecauseOf(reason: Reason) =
    this is BingeBotException &&
        this.reason == reason