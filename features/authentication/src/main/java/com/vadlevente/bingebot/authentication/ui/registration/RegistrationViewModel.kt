package com.vadlevente.bingebot.authentication.ui.registration

import com.vadlevente.bingebot.authentication.R
import com.vadlevente.bingebot.authentication.domain.usecase.RegistrationUseCase
import com.vadlevente.bingebot.authentication.domain.usecase.RegistrationUseCaseParams
import com.vadlevente.bingebot.authentication.ui.registration.RegistrationViewModel.ViewState
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastType.INFO
import com.vadlevente.bingebot.core.model.NavDestination
import com.vadlevente.bingebot.core.model.NavDestination.LOGIN
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.ui.BaseViewModel
import com.vadlevente.bingebot.core.ui.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    private val registrationUseCase: RegistrationUseCase,
) : BaseViewModel<ViewState>(
    navigationEventChannel, toastEventChannel
) {

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
            navigateTo(NavDestination.LIST_MOVIE)
        }
    }

    fun onNavigateToLogin() {
        navigateTo(LOGIN)
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