package com.bingebot.core.events.toast

import com.bingebot.core.events.EventChannel
import com.bingebot.core.events.EventChannelBase

interface ToastEventChannel : EventChannel<ToastEvent>

class ToastEventChannelImpl : EventChannelBase<ToastEvent>(), ToastEventChannel {
}

enum class ToastType {
    INFO,
    WARNING,
    ERROR,
}