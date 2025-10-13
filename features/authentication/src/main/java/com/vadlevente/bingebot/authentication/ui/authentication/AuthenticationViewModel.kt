package com.vadlevente.bingebot.authentication.ui.authentication

import com.vadlevente.bingebot.authentication.domain.usecase.GetDecryptionCipherUseCase
import com.vadlevente.bingebot.authentication.domain.usecase.IsBiometricsEnrolledUseCase
import com.vadlevente.bingebot.authentication.domain.usecase.RetrieveSecretWithBiometricsUseCase
import com.vadlevente.bingebot.authentication.domain.usecase.RetrieveSecretWithBiometricsUseCaseParams
import com.vadlevente.bingebot.authentication.domain.usecase.RetrieveSecretWithPinUseCase
import com.vadlevente.bingebot.authentication.domain.usecase.RetrieveSecretWithPinUseCaseParams
import com.vadlevente.bingebot.core.delegates.AppCloserDelegate
import com.vadlevente.bingebot.core.events.navigation.NavigationEvent
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.Extras
import com.vadlevente.bingebot.core.usecase.SetScreenResultUseCase
import com.vadlevente.bingebot.core.usecase.SetScreenResultUseCaseArgs
import com.vadlevente.bingebot.core.util.Constants.PIN_LENGTH
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import javax.crypto.Cipher
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    isBiometricsEnrolledUseCase: IsBiometricsEnrolledUseCase,
    getDecryptionCipherUseCase: GetDecryptionCipherUseCase,
    private val appCloserDelegate: AppCloserDelegate,
    private val retrieveSecretWithPinUseCase: RetrieveSecretWithPinUseCase,
    private val retrieveSecretWithBiometricsUseCase: RetrieveSecretWithBiometricsUseCase,
    private val setScreenResultUseCase: SetScreenResultUseCase,
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

    init {
        combine(
            isBiometricsEnrolledUseCase.execute(Unit),
            getDecryptionCipherUseCase.execute(Unit),
            ::Pair
        ).onValue { (isBiometricsEnrolled, cipher) ->
            if (isBiometricsEnrolled) {
                viewState.update {
                    it.copy(
                        showBiometricPrompt = true,
                        cipher = cipher,
                        isBiometricsEnrolled = true,
                    )
                }
            }
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
                closeFlow()
            }
        }
    }

    fun onOpenBiometricAuthentication() {
        viewState.update {
            it.copy(
                showBiometricPrompt = true,
            )
        }
    }

    fun onBiometricAuthSuccessful(cipher: Cipher) {
        viewState.update {
            it.copy(
                showBiometricPrompt = false,
                cipher = null,
            )
        }
        retrieveSecretWithBiometricsUseCase.execute(
            RetrieveSecretWithBiometricsUseCaseParams(
                cipher = cipher
            )
        ).onValue {
            closeFlow()
        }
    }

    fun onBiometricPromptCancelled() {
        viewState.update {
            it.copy(
                showBiometricPrompt = false
            )
        }
    }

    private suspend fun closeFlow() {
        setScreenResultUseCase.execute(
            SetScreenResultUseCaseArgs(Extras.AUTHENTICATION_FINISHED)
        ).onStartSilent()
        navigationEventChannel.sendEvent(
            NavigationEvent.TopNavigationEvent.NavigateUp
        )
    }

    data class ViewState(
        val pin: String = "",
        val showBiometricPrompt: Boolean = false,
        val cipher: Cipher? = null,
        val isBiometricsEnrolled: Boolean = false,
    ) : State

}