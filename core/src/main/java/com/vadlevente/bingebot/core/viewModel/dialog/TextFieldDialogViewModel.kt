package com.vadlevente.bingebot.core.viewModel.dialog

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.core.events.dialog.DialogEvent.ShowTextFieldDialog
import com.vadlevente.bingebot.core.events.dialog.DialogEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.State
import com.vadlevente.bingebot.core.viewModel.dialog.TextFieldDialogViewModel.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TextFieldDialogViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    dialogEventChannel: DialogEventChannel,
) : BaseViewModel<ViewState>(
    navigationEventChannel, toastEventChannel
) {

    private val viewState = MutableStateFlow(ViewState())
    override val state: StateFlow<ViewState> = viewState

    init {
        dialogEventChannel.events.filterIsInstance<ShowTextFieldDialog>().onEach { event ->
            viewState.update {
                it.copy(
                    isVisible = true,
                    event = event,
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onDismiss() {
        viewState.update {
            it.copy(
                isVisible = false,
            )
        }
    }

    fun onPositiveClicked() {
        viewState.value.event?.let {
            it.onPositiveButtonClicked(viewState.value.text)
        }
        onDismiss()
    }

    fun onNegativeClicked() {
        onDismiss()
    }

    fun onTextChanged(value: String) {
        viewState.update {
            it.copy(
                text = value,
            )
        }
    }

    data class ViewState(
        val isVisible: Boolean = false,
        val event: ShowTextFieldDialog? = null,
        val text: String = "",
    ) : State

}