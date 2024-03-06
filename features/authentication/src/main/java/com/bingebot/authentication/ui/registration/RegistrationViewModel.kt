package com.bingebot.authentication.ui.login

import com.bingebot.authentication.domain.usecase.RegistrationUseCase
import com.bingebot.authentication.domain.usecase.RegistrationUseCaseParams
import com.bingebot.authentication.ui.login.RegistrationViewModel.ViewState
import com.bingebot.core.model.NavDestination
import com.bingebot.core.ui.BaseViewModel
import com.bingebot.core.ui.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registrationUseCase: RegistrationUseCase,
) : BaseViewModel<ViewState>() {

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
        registrationUseCase.execute(
            RegistrationUseCaseParams(
            viewState.value.email,
            viewState.value.password,
        )
        ).onValue {
            navigateTo(NavDestination.LIST)
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