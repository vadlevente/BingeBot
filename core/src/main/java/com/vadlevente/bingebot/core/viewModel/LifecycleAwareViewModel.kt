package com.vadlevente.bingebot.core.viewModel

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.core.events.navigation.NavigationEvent
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.NavDestination
import com.vadlevente.bingebot.core.usecase.ShouldAuthenticateUseCase
import kotlinx.coroutines.launch

abstract class LifecycleAwareViewModel <S : State> (
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    private val shouldAuthenticateUseCase: ShouldAuthenticateUseCase,
) : BaseViewModel<S>(navigationEventChannel, toastEventChannel), DefaultLifecycleObserver {

    open fun onAuthenticationHandled(authOpened: Boolean) {}

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        shouldAuthenticateUseCase.execute(Unit).onValue { shouldAuthenticate ->
            if (shouldAuthenticate) {
                viewModelScope.launch {
                    navigationEventChannel.sendEvent(
                        NavigationEvent.TopNavigationEvent.NavigateTo(NavDestination.TopNavDestination.Authenticate)
                    )
                }
            }
            onAuthenticationHandled(shouldAuthenticate)
        }
    }

}