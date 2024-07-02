package com.vadlevente.bingebot.core.events.dialog

import com.vadlevente.bingebot.core.events.EventChannel
import com.vadlevente.bingebot.core.events.EventChannelBase
import com.vadlevente.bingebot.core.events.dialog.DialogEvent.ShowDialog
import com.vadlevente.bingebot.core.events.dialog.DialogResponse.NEGATIVE
import com.vadlevente.bingebot.core.events.dialog.DialogResponse.POSITIVE
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

enum class DialogResponse {
    POSITIVE,
    NEGATIVE,
}

interface DialogEventChannel : EventChannel<DialogEvent> {
    suspend fun showDialog(event: ShowDialog) = MutableSharedFlow<DialogResponse>()
        .apply {
            sendEvent(
                event.copy(
                    onPositiveButtonClicked = { tryEmit(POSITIVE) },
                    onNegativeButtonClicked = { tryEmit(NEGATIVE) },
                )
            )
        }.asSharedFlow()
}

class DialogEventChannelImpl @Inject constructor(

) : EventChannelBase<DialogEvent>(), DialogEventChannel