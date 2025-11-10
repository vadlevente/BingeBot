package com.vadlevente.bingebot.authentication.ui.pin

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.authentication.R
import com.vadlevente.bingebot.authentication.domain.usecase.IsBiometricsAvailableUseCase
import com.vadlevente.bingebot.authentication.domain.usecase.SaveSecretWithPinUseCase
import com.vadlevente.bingebot.authentication.domain.usecase.SaveSecretWithPinUseCaseParams
import com.vadlevente.bingebot.authentication.ui.pin.RegisterPinViewModel.ViewState
import com.vadlevente.bingebot.core.delegates.AppCloserDelegate
import com.vadlevente.bingebot.core.events.navigation.NavigationEvent
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastType
import com.vadlevente.bingebot.core.model.Extras
import com.vadlevente.bingebot.core.model.NavDestination
import com.vadlevente.bingebot.core.ui.stringOf
import com.vadlevente.bingebot.core.usecase.GetConfigurationUseCase
import com.vadlevente.bingebot.core.usecase.SetScreenResultUseCase
import com.vadlevente.bingebot.core.usecase.SetScreenResultUseCaseArgs
import com.vadlevente.bingebot.core.util.Constants.PIN_LENGTH
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.State
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = RegisterPinViewModel.RegisterPinViewModelFactory::class)
class RegisterPinViewModel @AssistedInject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    private val appCloserDelegate: AppCloserDelegate,
    private val saveSecretWithPinUseCase: SaveSecretWithPinUseCase,
    private val isBiometricsAvailableUseCase: IsBiometricsAvailableUseCase,
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val setScreenResultUseCase: SetScreenResultUseCase,
    @Assisted("email") val email: String,
    @Assisted("password") val password: String,
) : BaseViewModel<ViewState>(
    navigationEventChannel, toastEventChannel
), AppCloserDelegate by appCloserDelegate {

    private val viewState = MutableStateFlow(ViewState())
    override val state: StateFlow<ViewState> = viewState

    fun onPinChanged(value: String) {
        viewState.update {
            it.copy(
                pin = value
            )
        }
        if (value.length == PIN_LENGTH) {
            viewModelScope.launch {
                navigationEventChannel.sendEvent(
                    NavigationEvent.EnrollSecurityNavigationEvent.NavigateTo(
                        NavDestination.EnrollSecurityNavDestination.RegisterPinConfirm
                    )
                )
            }
        }
    }

    fun onPinConfirmChanged(value: String) {
        viewState.update {
            it.copy(
                pinConfirmed = value
            )
        }
        if (value.length == PIN_LENGTH) {
            if (value == viewState.value.pin) {
                saveSecretWithPinUseCase.execute(
                    SaveSecretWithPinUseCaseParams(
                        pin = value,
                        email = email,
                        password = password
                    )
                ).onValue {
                    combine(
                        getConfigurationUseCase.execute(Unit),
                        isBiometricsAvailableUseCase.execute(Unit),
                        ::Pair,
                    ).onValue { (_, isBiometricsAvailable) ->
                        viewState.update {
                            it.copy(
                                pin = "",
                                pinConfirmed = "",
                            )
                        }
                        if (isBiometricsAvailable) {
                            viewModelScope.launch {
                                navigationEventChannel.sendEvent(
                                    NavigationEvent.EnrollSecurityNavigationEvent.NavigateTo(
                                        NavDestination.EnrollSecurityNavDestination.BiometricsRegistration(
                                            email,
                                            password
                                        )
                                    )
                                )
                            }
                        } else {
                            showToast(
                                stringOf(R.string.pin_registration_successful),
                                ToastType.INFO
                            )
                            setScreenResultUseCase.execute(
                                SetScreenResultUseCaseArgs(Extras.SECURITY_ENROLLMENT_FINISHED)
                            ).onStartSilent()
                            viewModelScope.launch {
                                navigationEventChannel.sendEvent(
                                    NavigationEvent.TopNavigationEvent.NavigateUp
                                )
                            }
                        }
                    }
                }
            } else {
                showToast(stringOf(R.string.pin_codesNotEqual), ToastType.ERROR)
                viewState.update {
                    it.copy(
                        pinConfirmed = ""
                    )
                }
            }
        }
    }

    fun onExitRegistration() {
        viewModelScope.launch {
            showExitConfirmation()
        }
    }

    fun onCloseFlow() {
        viewModelScope.launch {
            navigationEventChannel.sendEvent(
                NavigationEvent.TopNavigationEvent.NavigateUp
            )
        }
    }

    fun onBackPressed() {
        viewState.update {
            it.copy(
                pin = "",
                pinConfirmed = "",
            )
        }
        viewModelScope.launch {
            navigationEventChannel.sendEvent(
                NavigationEvent.EnrollSecurityNavigationEvent.NavigateUp
            )
        }
    }

    @AssistedFactory
    interface RegisterPinViewModelFactory {
        fun create(
            @Assisted("email") email: String,
            @Assisted("password") password: String,
        ): RegisterPinViewModel
    }

    data class ViewState(
        val pin: String = "",
        val pinConfirmed: String = "",
        val isSubmitEnabled: Boolean = false,
    ) : State

}