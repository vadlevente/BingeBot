package com.vadlevente.bingebot.authentication.ui.login

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.authentication.R
import com.vadlevente.bingebot.authentication.domain.usecase.LoginUseCase
import com.vadlevente.bingebot.authentication.domain.usecase.LoginUseCaseParams
import com.vadlevente.bingebot.authentication.domain.usecase.ResendPasswordUseCase
import com.vadlevente.bingebot.authentication.domain.usecase.ResendPasswordUseCaseParams
import com.vadlevente.bingebot.authentication.ui.login.LoginViewModel.ViewState
import com.vadlevente.bingebot.core.delegates.AppCloserDelegate
import com.vadlevente.bingebot.core.events.navigation.NavigationEvent
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastType.INFO
import com.vadlevente.bingebot.core.model.NavDestination.NonAuthenticatedNavDestination
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    private val loginUseCase: LoginUseCase,
    private val resendPasswordUseCase: ResendPasswordUseCase,
    private val appCloserDelegate: AppCloserDelegate,
) : BaseViewModel<ViewState>(
    navigationEventChannel, toastEventChannel
), AppCloserDelegate by appCloserDelegate {

    private val viewState = MutableStateFlow(ViewState())
    override val state: StateFlow<ViewState> = viewState

    fun onEmailChanged(newValue: String) {
        viewState.update { it.copy(email = newValue) }
        reevaluateSubmitEnabled()
        viewState.update { it.copy(isResendPasswordEnabled = Patterns.EMAIL_ADDRESS.matcher(newValue).matches()) }
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
            showToast(
                stringOf(R.string.loginSuccessful),
                INFO,
            )
            navigationEventChannel.sendEvent(
                NavigationEvent.NonAuthenticatedNavigationEvent.NavigateTo(
                    NonAuthenticatedNavDestination.RegisterPin(
                        viewState.value.email,
                        viewState.value.password,
                    )
                )
            )
        }
    }

    fun onNavigateToRegistration() {
        viewModelScope.launch {
            navigationEventChannel.sendEvent(
                NavigationEvent.NonAuthenticatedNavigationEvent.NavigateTo(
                    (NonAuthenticatedNavDestination.Registration)
                )
            )
        }
    }

    fun onResendPassword() {
        resendPasswordUseCase.execute(
            ResendPasswordUseCaseParams(viewState.value.email)
        ).onValue {
            showToast(
                stringOf(R.string.passwordResendSuccessful),
                INFO,
            )
        }
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
        val isResendPasswordEnabled: Boolean = false,
    ) : State

}