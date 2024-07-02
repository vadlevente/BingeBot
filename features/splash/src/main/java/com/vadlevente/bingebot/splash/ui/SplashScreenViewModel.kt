package com.vadlevente.bingebot.splash.ui

import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.ui.BaseViewModel
import com.vadlevente.bingebot.core.ui.EmptyState
import com.vadlevente.bingebot.splash.usecase.GetNavDestinationToStartScreenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    getNavDestinationToStartScreen: GetNavDestinationToStartScreenUseCase,
) : BaseViewModel<EmptyState>(
    navigationEventChannel, toastEventChannel
) {

    override val state = MutableStateFlow(EmptyState)

    init {
        getNavDestinationToStartScreen.execute(Unit)
            .onValue {
                navigateTo(it)
            }
    }

}