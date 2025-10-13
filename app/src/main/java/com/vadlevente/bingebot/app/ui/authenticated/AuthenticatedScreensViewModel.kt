package com.vadlevente.bingebot.app.ui.authenticated

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.Extras
import com.vadlevente.bingebot.core.usecase.GetScreenResultUseCase
import com.vadlevente.bingebot.core.usecase.GetScreenResultUseCaseArgs
import com.vadlevente.bingebot.core.usecase.ShouldAuthenticateUseCase
import com.vadlevente.bingebot.core.viewModel.LifecycleAwareViewModel
import com.vadlevente.bingebot.core.viewModel.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticatedScreensViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    shouldAuthenticateUseCase: ShouldAuthenticateUseCase,
    getScreenResultUseCase: GetScreenResultUseCase,
) : LifecycleAwareViewModel<AuthenticatedScreensViewModel.ViewState>(
navigationEventChannel, toastEventChannel, shouldAuthenticateUseCase
)  {
    private val viewState = MutableStateFlow(ViewState())
    override val state = viewState.asStateFlow()

    init {
        getScreenResultUseCase.execute(
            GetScreenResultUseCaseArgs(Extras.AUTHENTICATION_FINISHED)
        ).filter { it }.onValue {
            onAuthenticationHandled()
        }
    }

    override fun onAuthenticationHandled() {
        viewModelScope.launch {
            viewState.update {
                it.copy(
                    shouldShowScreens = true,
                )
            }
        }
    }

    data class ViewState(
        val shouldShowScreens: Boolean = false
    ) : State
}