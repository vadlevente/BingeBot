package com.vadlevente.bingebot.core.events.dialog

import com.vadlevente.bingebot.core.UIText

sealed interface DialogEvent {

    data class ShowDialog(
        val title: UIText? = null,
        val content: UIText? = null,
        val positiveButtonTitle: UIText?,
        val negativeButtonTitle: UIText? = null,
        val isCancelable: Boolean = true,
        val onPositiveButtonClicked: suspend () -> Unit = {},
        val onNegativeButtonClicked: () -> Unit = {},
    ) : DialogEvent

    data class ShowTextFieldDialog(
        val title: UIText? = null,
        val content: UIText? = null,
        val positiveButtonTitle: UIText?,
        val negativeButtonTitle: UIText? = null,
        val isCancelable: Boolean = true,
        val onPositiveButtonClicked: (String) -> Unit = {},
        val onNegativeButtonClicked: () -> Unit = {},
    ) : DialogEvent

}