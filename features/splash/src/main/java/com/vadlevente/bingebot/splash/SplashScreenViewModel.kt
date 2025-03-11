package com.vadlevente.bingebot.splash

import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.usecase.GetConfigurationUseCase
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.EmptyState
import com.vadlevente.bingebot.splash.usecase.GetNavDestinationToStartScreenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    getNavDestinationToStartScreen: GetNavDestinationToStartScreenUseCase,
    getConfigurationUseCase: GetConfigurationUseCase,
) : BaseViewModel<EmptyState>(
    navigationEventChannel, toastEventChannel
) {

    override val state = MutableStateFlow(EmptyState)

    init {
        combine(
            getConfigurationUseCase.execute(Unit),
            getNavDestinationToStartScreen.execute(Unit),
            ::Pair
        )
            .onValue { (_, navDestination) ->
                navigateTo(navDestination)
            }
    }

}