package com.bingebot.core.events.dialog

import com.bingebot.core.events.EventChannel
import com.bingebot.core.events.EventChannelBase
import javax.inject.Inject

enum class DialogResponse {
    POSITIVE,
    NEGATIVE,
}

interface DialogEventChannel : EventChannel<DialogEvent>

class DialogEventChannelImpl @Inject constructor(

) : EventChannelBase<DialogEvent>(), DialogEventChannel