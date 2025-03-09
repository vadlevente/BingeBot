package com.vadlevente.moviedetails

import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.model.SkeletonFactory
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.State
import com.vadlevente.moviedetails.ItemDetailsViewModel.ViewState
import com.vadlevente.moviedetails.domain.model.DisplayedItemDetails
import com.vadlevente.moviedetails.domain.usecases.GetItemDetailsUseCase
import com.vadlevente.moviedetails.domain.usecases.GetItemDetailsUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

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
    abstract val skeletonFactory: SkeletonFactory<T>

    abstract fun onNavigateToOptions()

    init {
        getItemDetailsUseCase.execute(
            GetItemDetailsUseCaseParams(
                itemId = itemId
            )
        ).onValue { details ->
            baseViewState.update {
                it.copy(
                    details = details
                )
            }
        }
    }

    fun onBackPressed() {
        navigateUp()
    }

    data class ViewState<T : Item>(
        val details: DisplayedItemDetails<T>? = null,
    ) : State

}