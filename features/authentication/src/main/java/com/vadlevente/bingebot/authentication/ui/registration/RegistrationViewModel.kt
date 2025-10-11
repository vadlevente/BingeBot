package com.vadlevente.bingebot.authentication.ui.registration

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.authentication.R
import com.vadlevente.bingebot.authentication.domain.usecase.RegistrationUseCase
import com.vadlevente.bingebot.authentication.domain.usecase.RegistrationUseCaseParams
import com.vadlevente.bingebot.authentication.ui.registration.RegistrationViewModel.ViewState
import com.vadlevente.bingebot.core.delegates.AppCloserDelegate
import com.vadlevente.bingebot.core.events.dialog.DialogEvent.ShowDialog
import com.vadlevente.bingebot.core.events.dialog.DialogEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEvent
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastType.INFO
import com.vadlevente.bingebot.core.model.Extras
import com.vadlevente.bingebot.core.model.NavDestination
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
class RegistrationViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    getScreenResultUseCase: GetScreenResultUseCase,
    private val registrationUseCase: RegistrationUseCase,
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
    }

    fun onPasswordChanged(newValue: String) {
        viewState.update { it.copy(password = newValue) }
        reevaluateSubmitEnabled()
    }

    fun onConfirmedPasswordChanged(newValue: String) {
        viewState.update { it.copy(passwordConfirmed = newValue) }
        reevaluateSubmitEnabled()
    }

    fun onSubmit() {
        registrationUseCase.execute(
            RegistrationUseCaseParams(
                viewState.value.email,
                viewState.value.password,
            )
        ).onValue {
            showToast(
                stringOf(R.string.registrationSuccessful),
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

    fun onNavigateToLogin() {
        viewModelScope.launch {
            navigationEventChannel.sendEvent(
                NavigationEvent.OnboardingNavigationEvent.NavigateTo(
                    NavDestination.OnboardingNavDestination.Login
                )
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
            val passwordsMatch = it.password.isNotEmpty() && it.password == it.passwordConfirmed
            it.copy(
                passwordsDoNotMatch = !passwordsMatch && it.passwordConfirmed.isNotEmpty(),
                isSubmitEnabled = it.email.isNotEmpty() && passwordsMatch
            )
        }
    }

    data class ViewState(
        val email: String = "",
        val password: String = "",
        val passwordConfirmed: String = "",
        val isSubmitEnabled: Boolean = false,
        val passwordsDoNotMatch: Boolean = false,
    ) : State

}