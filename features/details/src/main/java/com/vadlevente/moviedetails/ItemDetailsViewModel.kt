package com.vadlevente.moviedetails

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.core.events.navigation.NavigationEvent
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.model.NavDestination
import com.vadlevente.bingebot.core.util.Constants.LOADING_DELAY_MS
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.State
import com.vadlevente.moviedetails.ItemDetailsViewModel.ViewState
import com.vadlevente.moviedetails.domain.model.DisplayedItemDetails
import com.vadlevente.moviedetails.domain.usecases.GetItemDetailsUseCase
import com.vadlevente.moviedetails.domain.usecases.GetItemDetailsUseCaseParams
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class ItemDetailsViewModel<T : Item>(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    private val getItemDetailsUseCase: GetItemDetailsUseCase<T>,
    private val itemId: Int,
) : BaseViewModel<ViewState<T>>(
    navigationEventChannel, toastEventChannel
) {

    protected val baseViewState = MutableStateFlow(ViewState<T>())
    override val state: StateFlow<ViewState<T>> = baseViewState

    abstract fun onNavigateToOptions()

    private var isInitialized = false

    init {
        getItemDetailsUseCase.execute(
            GetItemDetailsUseCaseParams(
                itemId = itemId
            )
        ).onValue { details ->
            if (!isInitialized) {
                isInProgress.value = true
                baseViewState.update {
                    it.copy(
                        details = details
                    )
                }
                delay(LOADING_DELAY_MS)
                isInProgress.value = false
                isInitialized = true
            } else {
                baseViewState.update {
                    it.copy(
                        details = details
                    )
                }
            }
        }
    }

    fun onBackPressed() {
        viewModelScope.launch {
            navigationEventChannel.sendEvent(
                NavigationEvent.AuthenticatedNavigationEvent.NavigateUp
            )
        }
    }

    fun onCastMemberClicked(personId: Int) {
        viewModelScope.launch {
            navigationEventChannel.sendEvent(
                NavigationEvent.AuthenticatedNavigationEvent.NavigateTo(
                    NavDestination.AuthenticatedNavDestination.PersonDetails(personId)
                )
            )
        }
    }

    data class ViewState<T : Item>(
        val details: DisplayedItemDetails<T>? = null,
    ) : State

}