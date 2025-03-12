package com.vadlevente.bingebot.app.ui.authenticated

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.usecase.ShouldAuthenticateUseCase
import com.vadlevente.bingebot.core.viewModel.LifecycleAwareViewModel
import com.vadlevente.bingebot.core.viewModel.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticatedScreensViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    shouldAuthenticateUseCase: ShouldAuthenticateUseCase,
) : LifecycleAwareViewModel<AuthenticatedScreensViewModel.ViewState>(
navigationEventChannel, toastEventChannel, shouldAuthenticateUseCase
)  {
    private val viewState = MutableStateFlow(ViewState())
    override val state = viewState.asStateFlow()

    override fun onAuthenticationHandled(authOpened: Boolean) {
        viewModelScope.launch {
            if (authOpened) {
                delay(1500)
            }
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