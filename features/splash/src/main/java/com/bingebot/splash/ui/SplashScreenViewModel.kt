package com.bingebot.splash.ui

import com.bingebot.core.ui.BaseViewModel
import com.bingebot.core.ui.EmptyState
import com.bingebot.splash.usecase.GetNavDestinationToStartScreenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class SplashScreenViewModel @Inject constructor(
    getNavDestinationToStartScreen: GetNavDestinationToStartScreenUseCase,
) : BaseViewModel<EmptyState>() {

    override val state = MutableStateFlow(EmptyState)

    init {
        getNavDestinationToStartScreen.execute(Unit)
            .onValue {
                navigateTo(it)
            }
    }

}