package com.vadlevente.bingebot.search

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEvent
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.DisplayedItem
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.util.Constants.QUERY_MINIMUM_LENGTH
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.State
import com.vadlevente.bingebot.search.SearchItemViewModel.ViewState
import com.vadlevente.bingebot.search.usecase.GetSearchResultUseCase
import com.vadlevente.bingebot.search.usecase.SearchItemUseCase
import com.vadlevente.bingebot.search.usecase.SearchItemUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class SearchItemViewModel<T : Item>(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    private val getSearchResultUseCase: GetSearchResultUseCase<T>,
    private val searchItemUseCase: SearchItemUseCase<T>,
    private val bottomSheetEventChannel: BottomSheetEventChannel,
) : BaseViewModel<ViewState<T>>(
    navigationEventChannel, toastEventChannel
) {

    protected val viewState = MutableStateFlow(ViewState<T>())
    override val state: StateFlow<ViewState<T>> = viewState

    init {
        getSearchResultUseCase.execute(Unit)
            .onValue { items ->
                viewState.update {
                    it.copy(
                        items = items,
                    )
                }
            }
    }

    abstract fun onNavigateToOptions(itemId: Int)

    fun onQueryChanged(query: String) {
        viewState.update {
            it.copy(
                query = query,
            )
        }
        if (query.length < QUERY_MINIMUM_LENGTH) return
        searchItemUseCase.execute(
            SearchItemUseCaseParams(query)
        ).onStart()
    }

    fun onBackPressed() {
        viewModelScope.launch {
            navigationEventChannel.sendEvent(
                NavigationEvent.AuthenticatedNavigationEvent.NavigateUp
            )
        }
    }

    data class ViewState<T : Item>(
        val query: String = "",
        val items: List<DisplayedItem<T>> = emptyList(),
    ) : State

}