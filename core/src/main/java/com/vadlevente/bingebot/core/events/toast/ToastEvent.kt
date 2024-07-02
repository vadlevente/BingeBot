package com.vadlevente.bingebot.core.events.toast

import com.vadlevente.bingebot.core.UIText

sealed interface ToastEvent {
    data class ShowToast(
        val message: UIText,
        val type: ToastType,
    ) : ToastEvent
}