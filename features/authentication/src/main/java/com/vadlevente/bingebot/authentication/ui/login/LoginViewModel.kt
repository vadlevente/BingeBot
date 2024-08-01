package com.vadlevente.bingebot.authentication.ui.login

import com.vadlevente.bingebot.authentication.domain.usecase.LoginUseCase
import com.vadlevente.bingebot.authentication.domain.usecase.LoginUseCaseParams
import com.vadlevente.bingebot.authentication.ui.login.LoginViewModel.ViewState
import com.vadlevente.bingebot.core.delegates.AppCloserDelegate
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.NavDestination
import com.vadlevente.bingebot.core.model.NavDestination.REGISTRATION
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    private val loginUseCase: LoginUseCase,
    private val appCloserDelegate: AppCloserDelegate,
) : BaseViewModel<ViewState>(
    navigationEventChannel, toastEventChannel
), AppCloserDelegate by appCloserDelegate {

    private val viewState = MutableStateFlow(ViewState())
    override val state: StateFlow<ViewState> = viewState

    fun onEmailChanged(newValue: String) {
        viewState.update { it.copy(email = newValue) }
        reevaluateSubmitEnabled()
    }

    fun onPasswordChanged(newValue: String) {
        viewState.update { it.copy(password = newValue) }
        reevaluateSubmitEnabled()
    }

    fun onSubmit() {
        loginUseCase.execute(
            LoginUseCaseParams(
            viewState.value.email,
            viewState.value.password,
        )
        ).onValue {
            navigateTo(NavDestination.DASHBOARD)
        }
    }

    fun onNavigateToRegistration() {
        navigateTo(REGISTRATION)
    }

    private fun reevaluateSubmitEnabled() {
        viewState.update {
            it.copy(
                isSubmitEnabled = it.email.isNotEmpty() && it.password.isNotEmpty()
            )
        }
    }

    data class ViewState(
        val email: String = "",
        val password: String = "",
        val isSubmitEnabled: Boolean = false,
    ) : State

}