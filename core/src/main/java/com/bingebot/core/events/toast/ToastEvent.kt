package com.bingebot.core.events.toast

import com.bingebot.core.UIText

sealed interface ToastEvent {
    data class ShowToast(
        val message: UIText,
        val type: ToastType,
    )
}