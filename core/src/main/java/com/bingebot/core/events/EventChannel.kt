package com.bingebot.core.events

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

interface EventChannel<T> {
    val events: SharedFlow<T>

    suspend fun sendEvent(vararg event: T)
}

abstract class EventChannelBase<T: Any> : EventChannel<T> {
    override val events = MutableSharedFlow<T>()

    override suspend fun sendEvent(vararg event: T) {
        event.toList().forEach {
            events.emit(it)
        }
    }
}
