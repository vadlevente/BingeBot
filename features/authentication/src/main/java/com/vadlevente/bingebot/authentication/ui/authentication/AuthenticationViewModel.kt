package com.vadlevente.bingebot.authentication.ui.authentication

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.authentication.domain.usecase.RetrieveSecretWithPinUseCase
import com.vadlevente.bingebot.authentication.domain.usecase.RetrieveSecretWithPinUseCaseParams
import com.vadlevente.bingebot.core.delegates.AppCloserDelegate
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.NavDestination
import com.vadlevente.bingebot.core.util.Constants.PIN_LENGTH
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    private val appCloserDelegate: AppCloserDelegate,
    private val retrieveSecretWithPinUseCase: RetrieveSecretWithPinUseCase,
) : BaseViewModel<AuthenticationViewModel.ViewState>(
    navigationEventChannel, toastEventChannel
), AppCloserDelegate by appCloserDelegate {

    private val viewState = MutableStateFlow(ViewState())
    override val state: StateFlow<ViewState> = viewState

    override val basicErrorHandler: (Throwable) -> Unit = {
        super.basicErrorHandler(it)
        viewState.update {
            it.copy(
                pin = ""
            )
        }
    }

    fun onPinChanged(value: String) {
        viewState.update {
            it.copy(
                pin = value
            )
        }
        if (value.length == PIN_LENGTH) {
            retrieveSecretWithPinUseCase.execute(
                RetrieveSecretWithPinUseCaseParams(
                    pin = value
                )
            ).onValue {
                navigateTo(NavDestination.DASHBOARD)
            }
        }
    }

    fun onExitAuthentication() {
        viewModelScope.launch {
            showExitConfirmation()
        }
    }

    data class ViewState(
        val pin: String = ""
    ) : State

}