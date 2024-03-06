package com.bingebot.authentication.ui.login

import com.bingebot.authentication.domain.usecase.LoginUseCase
import com.bingebot.authentication.domain.usecase.LoginUseCaseParams
import com.bingebot.authentication.ui.login.LoginViewModel.ViewState
import com.bingebot.core.model.NavDestination
import com.bingebot.core.ui.BaseViewModel
import com.bingebot.core.ui.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
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
        loginUseCase.execute(LoginUseCaseParams(
            viewState.value.email,
            viewState.value.password,
        )).onValue {
            navigateTo(NavDestination.LIST)
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
    ) : State

}