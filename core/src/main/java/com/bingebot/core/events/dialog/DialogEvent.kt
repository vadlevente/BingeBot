package com.bingebot.core.events.dialog

import com.bingebot.core.UIText

sealed interface DialogEvent {

    data class ShowDialog(
        val title: UIText,
        val content: UIText,
        val positiveButtonTitle: UIText?,
        val negativeButtonTitle: UIText? = null,
        val isCancelable: Boolean = true,
        val onPositiveButtonClicked: () -> Unit = {},
        val onNegativeButtonClicked: () -> Unit = {},
    ) : DialogEvent

}