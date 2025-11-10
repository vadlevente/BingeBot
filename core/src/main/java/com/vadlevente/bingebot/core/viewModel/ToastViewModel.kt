package com.vadlevente.bingebot.core.viewModel

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEvent.ShowToast
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastType
import com.vadlevente.bingebot.core.events.toast.ToastType.WARNING
import com.vadlevente.bingebot.core.ui.UIText
import com.vadlevente.bingebot.core.ui.stringOf
import com.vadlevente.bingebot.core.viewModel.ToastViewModel.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ToastViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
) : BaseViewModel<ViewState>(
    navigationEventChannel, toastEventChannel
) {

    private val viewState = MutableStateFlow(ViewState())
    override val state: StateFlow<ViewState> = viewState

    init {
        toastEventChannel.events.filterIsInstance<ShowToast>().onEach { event ->
            viewState.update {
                it.copy(
                    isVisible = true,
                    text = event.message,
                    type = event.type,
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onHideToast() {
        viewState.update {
            it.copy(
                isVisible = false,
            )
        }
    }

    data class ViewState(
        val isVisible: Boolean = false,
        val text: UIText = stringOf(""),
        val type: ToastType = WARNING,
    ) : State

}