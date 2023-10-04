package com.bingebot.core.events.dialog

import com.bingebot.core.events.EventChannel
import com.bingebot.core.events.EventChannelBase

enum class DialogResponse {
    POSITIVE,
    NEGATIVE,
}

interface DialogEventChannel : EventChannel<DialogEvent>

class DialogEventChannelImpl : EventChannelBase<DialogEvent>(), DialogEventChannel