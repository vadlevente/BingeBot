package com.vadlevente.bingebot.authentication.ui.biometrics

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.authentication.R
import com.vadlevente.bingebot.authentication.domain.usecase.GetEncryptionCipherUseCase
import com.vadlevente.bingebot.authentication.domain.usecase.SaveSecretWithBiometricsUseCase
import com.vadlevente.bingebot.authentication.domain.usecase.SaveSecretWithBiometricsUseCaseParams
import com.vadlevente.bingebot.authentication.ui.biometrics.RegisterBiometricsViewModel.ViewState
import com.vadlevente.bingebot.core.delegates.AppCloserDelegate
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastType
import com.vadlevente.bingebot.core.model.NavDestination
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.State
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.crypto.Cipher

@HiltViewModel(assistedFactory = RegisterBiometricsViewModel.RegisterBiometricsViewModelFactory::class)
class RegisterBiometricsViewModel @AssistedInject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    getEncryptionCipherUseCase: GetEncryptionCipherUseCase,
    private val appCloserDelegate: AppCloserDelegate,
    private val saveSecretWithBiometricsUseCase: SaveSecretWithBiometricsUseCase,
    @Assisted("email") val email: String,
    @Assisted("password") val password: String,
) : BaseViewModel<ViewState>(
    navigationEventChannel, toastEventChannel
), AppCloserDelegate by appCloserDelegate {

    private val viewState = MutableStateFlow(ViewState())
    override val state: StateFlow<ViewState> = viewState

    init {
        getEncryptionCipherUseCase.execute(Unit).onValue { cipher ->
            viewState.update {
                it.copy(
                    cipher = cipher
                )
            }
        }
    }

    fun onConfirm() {
        viewState.update {
            it.copy(
                showBiometricPrompt = true
            )
        }
    }

    fun onCancel() {
        viewState.update {
            it.copy(
                showBiometricPrompt = false
            )
        }
        viewModelScope.launch {
            navigateTo(NavDestination.Dashboard)
        }
    }

    fun onAuthDismissed() {
        viewState.update {
            it.copy(
                showBiometricPrompt = false
            )
        }
    }

    fun onAuthSuccessful(cipher: Cipher) {
        viewState.update {
            it.copy(
                showBiometricPrompt = false
            )
        }
        saveSecretWithBiometricsUseCase.execute(
            SaveSecretWithBiometricsUseCaseParams(
                cipher = cipher,
                email = email,
                password = password,
            )
        ).onValue {
            showToast(stringOf(R.string.biometrics_registration_successful), ToastType.INFO)
            navigateTo(NavDestination.Dashboard)
        }
    }

    @AssistedFactory
    interface RegisterBiometricsViewModelFactory {
        fun create(
            @Assisted("email") email: String,
            @Assisted("password") password: String,
        ): RegisterBiometricsViewModel
    }

    data class ViewState(
        val showBiometricPrompt: Boolean = false,
        val cipher: Cipher? = null,
    ) : State

}