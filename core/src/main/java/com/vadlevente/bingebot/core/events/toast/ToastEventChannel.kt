package com.vadlevente.bingebot.core.events.toast

import com.vadlevente.bingebot.core.events.EventChannel
import com.vadlevente.bingebot.core.events.EventChannelBase
import javax.inject.Inject

interface ToastEventChannel : EventChannel<ToastEvent>

class ToastEventChannelImpl @Inject constructor(

) : EventChannelBase<ToastEvent>(), ToastEventChannel {
}

enum class ToastType {
    INFO,
    WARNING,
    ERROR,
}