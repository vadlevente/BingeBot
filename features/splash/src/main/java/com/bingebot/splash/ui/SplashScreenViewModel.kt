package com.bingebot.splash.ui

import androidx.lifecycle.viewModelScope
import com.bingebot.core.ui.BaseViewModel
import com.bingebot.core.ui.EmptyState
import com.bingebot.splash.usecase.GetNavDestinationToStartScreenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    getNavDestinationToStartScreen: GetNavDestinationToStartScreenUseCase,
) : BaseViewModel<EmptyState>() {

    override val state = MutableStateFlow(EmptyState)

    init {
        viewModelScope.launch {
            getNavDestinationToStartScreen.execute(Unit)
                .onEach {
                    var a = 1
                }
        }
        getNavDestinationToStartScreen.execute(Unit)
            .onValue {
                navigateTo(it)
            }
    }

}