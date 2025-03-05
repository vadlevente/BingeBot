package com.vadlevente.bingebot.core.model.exception

import com.vadlevente.bingebot.core.UIText
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.resources.R as Res

class BingeBotException(
    val originalException: Throwable? = null,
    val reason: Reason? = null,
    val errorMessage: UIText? = null,
    val silent: Boolean = false,
) : Throwable()

enum class Reason(val reasonText: UIText? = null) {
    AUTHENTICATION_FAILED(stringOf(Res.string.exception_authenticationFailed)),
    DATA_READ_ERROR,
    DATA_WRITE_ERROR,
    WEAK_PASSWORD(stringOf(Res.string.exception_weakPassword)),
    INVALID_CREDENTIALS(stringOf(Res.string.exception_invalidCredentials)),
    SESSION_EXPIRED(stringOf(Res.string.exception_sessionExpired)),
    WATCHLIST_ALREADY_EXISTS(stringOf(Res.string.exception_sessionExpired)),
    DATA_NOT_FOUND,
}

fun Throwable.isBecauseOf(reason: Reason) =
    this is BingeBotException &&
        this.reason == reason