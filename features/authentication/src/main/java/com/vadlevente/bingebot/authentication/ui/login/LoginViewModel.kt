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
import com.vadlevente.bingebot.core.events.dialog.DialogEvent.ShowDialog
import com.vadlevente.bingebot.core.events.dialog.DialogEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEvent
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastType.INFO
import com.vadlevente.bingebot.core.model.Extras
import com.vadlevente.bingebot.core.model.NavDestination
import com.vadlevente.bingebot.core.model.NavDestination.OnboardingNavDestination
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.usecase.GetScreenResultUseCase
import com.vadlevente.bingebot.core.usecase.GetScreenResultUseCaseArgs
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.vadlevente.bingebot.resources.R as Res

@HiltViewModel
class LoginViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    getScreenResultUseCase: GetScreenResultUseCase,
    private val loginUseCase: LoginUseCase,
    private val resendPasswordUseCase: ResendPasswordUseCase,
    private val appCloserDelegate: AppCloserDelegate,
    private val dialogEventChannel: DialogEventChannel,
) : BaseViewModel<ViewState>(
    navigationEventChannel, toastEventChannel
), AppCloserDelegate by appCloserDelegate {

    private val viewState = MutableStateFlow(ViewState())
    override val state: StateFlow<ViewState> = viewState

    init {
        getScreenResultUseCase.execute(
            GetScreenResultUseCaseArgs(Extras.SECURITY_ENROLLMENT_FINISHED)
        ).filter { it }.onValue {
            onNavigateToAuthenticatedScreens()
        }
    }

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
            dialogEventChannel.sendEvent(
                ShowDialog(
                    title = stringOf(R.string.pin_register_confirmation_title),
                    content = stringOf(R.string.pin_register_confirmation_description),
                    isCancelable = false,
                    positiveButtonTitle = stringOf(Res.string.common_Yes),
                    negativeButtonTitle = stringOf(Res.string.common_No),
                    onPositiveButtonClicked = {
                        navigationEventChannel.sendEvent(
                            NavigationEvent.TopNavigationEvent.NavigateTo(
                                NavDestination.TopNavDestination.EnrollSecurity(
                                    email = viewState.value.email,
                                    password = viewState.value.password,
                                    canStepBack = false,
                                )
                            )
                        )
                    },
                    onNegativeButtonClicked = {
                        onNavigateToAuthenticatedScreens()
                    }
                )
            )
        }
    }

    fun onNavigateToRegistration() {
        viewModelScope.launch {
            navigationEventChannel.sendEvent(
                NavigationEvent.OnboardingNavigationEvent.NavigateTo(
                    (OnboardingNavDestination.Registration)
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

    fun onNavigateToAuthenticatedScreens() {
        viewModelScope.launch {
            navigationEventChannel.sendEvent(
                NavigationEvent.TopNavigationEvent.NavigateTo(NavDestination.TopNavDestination.AuthenticatedScreens)
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