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
    EMAIL_ALREADY_IN_USE(stringOf(Res.string.exception_emailAlreadyInUse)),
    NON_EXISTENT_USER(stringOf(Res.string.exception_nonExistentUser)),
    SESSION_EXPIRED(stringOf(Res.string.exception_sessionExpired)),
    WRONG_PIN_CODE(stringOf(Res.string.exception_wrongPin)),
    WATCHLIST_ALREADY_EXISTS(stringOf(Res.string.exception_sessionExpired)),
    RESEND_UNSUCCESSFUL(stringOf(Res.string.exception_resendPasswordUnsuccessful)),
    DATA_NOT_FOUND,
    ENCRYPTION_ERROR,
}

fun Throwable.isBecauseOf(reason: Reason) =
    this is BingeBotException &&
        this.reason == reason