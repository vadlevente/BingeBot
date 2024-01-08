package com.bingebot.splash.ui

import androidx.lifecycle.viewModelScope
import com.bingebot.core.BaseViewModel
import com.bingebot.core.EmptyState
import com.bingebot.splash.usecase.NavigateToStartScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashScreenViewModel @Inject constructor(
    private val navigateToStartScreen: NavigateToStartScreen,
) : BaseViewModel<EmptyState>() {

    override val state = MutableStateFlow(EmptyState)

    init {
        viewModelScope.launch {
            navigateToStartScreen()
        }
    }

}