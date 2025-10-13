package com.vadlevente.bingebot.core.viewModel

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.core.events.navigation.NavigationEvent
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.NavDestination
import com.vadlevente.bingebot.core.usecase.ShouldAuthenticateUseCase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

abstract class LifecycleAwareViewModel<S : State>(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    private val shouldAuthenticateUseCase: ShouldAuthenticateUseCase,
) : BaseViewModel<S>(navigationEventChannel, toastEventChannel), DefaultLifecycleObserver {

    abstract fun onAuthenticationHandled()

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        viewModelScope.launch {
            val shouldAuthenticate = shouldAuthenticateUseCase.execute(Unit).firstOrNull()
            if (shouldAuthenticate == true) {
                navigationEventChannel.sendEvent(
                    NavigationEvent.TopNavigationEvent.NavigateTo(NavDestination.TopNavDestination.Authenticate)
                )
            } else {
                onAuthenticationHandled()
            }
        }
    }

}