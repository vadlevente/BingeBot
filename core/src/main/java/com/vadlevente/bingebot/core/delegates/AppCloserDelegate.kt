package com.vadlevente.bingebot.core.delegates

import com.vadlevente.bingebot.core.events.dialog.DialogEvent.ShowDialog
import com.vadlevente.bingebot.core.events.dialog.DialogEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEvent.ExitApplication
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.stringOf
import javax.inject.Inject
import com.vadlevente.bingebot.resources.R as Res

interface AppCloserDelegate {
    suspend fun showExitConfirmation()
}

class AppCloserDelegateImpl @Inject constructor(
    private val navigationEventChannel: NavigationEventChannel,
    private val dialogEventChannel: DialogEventChannel,
) : AppCloserDelegate {

    override suspend fun showExitConfirmation() {
        dialogEventChannel.sendEvent(
            ShowDialog(
                title = stringOf(Res.string.exit_dialogTitle),
                content = stringOf(Res.string.exit_dialogDesciption),
                positiveButtonTitle = stringOf(Res.string.common_Yes),
                negativeButtonTitle = stringOf(Res.string.common_Cancel),
                onPositiveButtonClicked = {
                    navigationEventChannel.sendEvent(ExitApplication)
                },
            )
        )
    }

}