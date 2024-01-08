package com.bingebot.core.model.exception

import com.bingebot.core.UIText

class BingeBotException(
    val reason: Reason
) : Throwable()

enum class Reason(val reasonText: UIText) {

}