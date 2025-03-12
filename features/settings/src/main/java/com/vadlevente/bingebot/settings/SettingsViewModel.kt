package com.vadlevente.bingebot.settings

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.core.events.dialog.DialogEvent.ShowDialog
import com.vadlevente.bingebot.core.events.dialog.DialogEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEvent
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.NavDestination
import com.vadlevente.bingebot.core.model.SelectedLanguage
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.State
import com.vadlevente.bingebot.resources.R.string
import com.vadlevente.bingebot.settings.SettingsViewModel.ViewState
import com.vadlevente.bingebot.settings.domain.usecases.GetLanguagesUseCase
import com.vadlevente.bingebot.settings.domain.usecases.GetUserEmailUseCase
import com.vadlevente.bingebot.settings.domain.usecases.LogoutUseCase
import com.vadlevente.bingebot.settings.domain.usecases.SetLanguageUseCase
import com.vadlevente.bingebot.settings.domain.usecases.SetLanguageUseCaseParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    getLanguagesUseCase: GetLanguagesUseCase,
    getUserEmailUseCase: GetUserEmailUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val dialogEventChannel: DialogEventChannel,
) : BaseViewModel<ViewState>(
    navigationEventChannel, toastEventChannel
) {

    private val viewState = MutableStateFlow(ViewState())
    override val state: StateFlow<ViewState> = viewState

    init {
        combine(
            getLanguagesUseCase.execute(Unit),
            getUserEmailUseCase.execute(Unit),
            ::Pair
        ).onValue { (languages, email) ->
            viewState.update {
                it.copy(
                    languages = languages,
                    email = email?.substringBefore("@"),
                )
            }
        }
    }

    fun onLanguageChanged(language: SelectedLanguage) {
        setLanguageUseCase.execute(
            SetLanguageUseCaseParams(language)
        ).onStartSilent()
    }

    fun onShowLanguageBottomSheet() {
        viewState.update {
            it.copy(
                showSelectLanguageBottomSheet = true,
            )
        }
    }

    fun onDismissLanguageBottomSheet() {
        viewState.update {
            it.copy(
                showSelectLanguageBottomSheet = false,
            )
        }
    }

    fun onLogout() {
        viewModelScope.launch {
            dialogEventChannel.sendEvent(
                ShowDialog(
                    title = stringOf(R.string.logoutDialog_title),
                    content = stringOf(R.string.logoutDialog_description),
                    positiveButtonTitle = stringOf(string.common_Yes),
                    negativeButtonTitle = stringOf(string.common_No),
                    onPositiveButtonClicked = {
                        logoutUseCase.execute(Unit).onValueSilent {
                            navigationEventChannel.sendEvent(
                                NavigationEvent.TopNavigationEvent.NavigateTo(NavDestination.TopNavDestination.NonAuthenticatedScreens)
                            )
                        }
                    },
                )
            )
        }
    }

    data class ViewState(
        val languages: Map<SelectedLanguage, Boolean> = emptyMap(),
        val showSelectLanguageBottomSheet: Boolean = false,
        val email: String? = null,
    ) : State

}