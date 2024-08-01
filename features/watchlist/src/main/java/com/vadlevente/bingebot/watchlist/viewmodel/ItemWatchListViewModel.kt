package com.vadlevente.bingebot.watchlist.viewmodel

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.core.events.dialog.DialogEvent.ShowDialog
import com.vadlevente.bingebot.core.events.dialog.DialogEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastType.INFO
import com.vadlevente.bingebot.core.model.DisplayedItem
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.model.exception.Reason.DATA_NOT_FOUND
import com.vadlevente.bingebot.core.model.exception.isBecauseOf
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.State
import com.vadlevente.bingebot.watchlist.R.string
import com.vadlevente.bingebot.watchlist.domain.usecase.DeleteWatchListUseCase
import com.vadlevente.bingebot.watchlist.domain.usecase.DeleteWatchListUseCaseParams
import com.vadlevente.bingebot.watchlist.domain.usecase.GetWatchListItemsUseCase
import com.vadlevente.bingebot.watchlist.domain.usecase.GetWatchListItemsUseCaseParams
import com.vadlevente.bingebot.watchlist.domain.usecase.GetWatchListUseCase
import com.vadlevente.bingebot.watchlist.domain.usecase.GetWatchListUseCaseParams
import com.vadlevente.bingebot.watchlist.viewmodel.ItemWatchListViewModel.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.vadlevente.bingebot.resources.R as Res

abstract class ItemWatchListViewModel <T : Item>(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    private val getWatchListItemsUseCase: GetWatchListItemsUseCase<T>,
    private val getWatchListUseCase: GetWatchListUseCase<T>,
    private val deleteWatchListUseCase: DeleteWatchListUseCase<T>,
    private val dialogEventChannel: DialogEventChannel,
) : BaseViewModel<ViewState<T>>(
    navigationEventChannel, toastEventChannel
) {

    protected val viewState = MutableStateFlow(ViewState<T>())
    override val state: StateFlow<ViewState<T>> = viewState
    override val basicErrorHandler: (Throwable) -> Unit = {
        if (it.isBecauseOf(DATA_NOT_FOUND)) {
            navigateUp()
        } else {
            super.basicErrorHandler(it)
        }
    }

    protected lateinit var watchListId: String

    abstract fun onNavigateToSearch()
    abstract fun onNavigateToOptions(itemId: Int)

    fun onInit(watchListId: String) {
        this.watchListId = watchListId
        getWatchListUseCase.execute(
            GetWatchListUseCaseParams(
                watchListId = watchListId
            )
        ).onValue { watchList ->
            viewState.update {
                it.copy(
                    title = watchList.title,
                )
            }
            getItems()
        }
    }

    fun onDeleteWatchList() {
        viewModelScope.launch {
            dialogEventChannel.sendEvent(
                ShowDialog(
                    title = stringOf(string.deleteWatchList_confirm_title),
                    content = stringOf(string.deleteWatchList_confirm_description),
                    positiveButtonTitle = stringOf(Res.string.common_Yes),
                    negativeButtonTitle = stringOf(Res.string.common_No),
                    onPositiveButtonClicked = {
                        deleteWatchListUseCase.execute(
                            DeleteWatchListUseCaseParams(
                                watchListId = watchListId
                            )
                        )
                            .onValue {
                                showToast(
                                    message = stringOf(Res.string.successfulDeleteToast),
                                    type = INFO,
                                )
                            }
                    },
                )
            )
        }
    }

    fun onToggleSearchField() {
        viewState.update {
            it.copy(
                isSearchFieldVisible = !it.isSearchFieldVisible,
                searchQuery = if (it.isSearchFieldVisible) null else "",
            )
        }
        getItems()
    }

    fun onQueryChanged(value: String) {
        viewState.update {
            it.copy(
                searchQuery = value,
            )
        }
        getItems()
    }

    fun onBackPressed() {
        navigateUp()
    }

    private fun getItems() {
        getWatchListItemsUseCase.execute(
            GetWatchListItemsUseCaseParams(
                watchListId = watchListId,
                query = viewState.value.searchQuery,
            )
        ).onValue { items ->
            viewState.update {
                it.copy(
                    items = items
                )
            }
        }
    }

    data class ViewState<T : Item>(
        val title: String? = null,
        val items: List<DisplayedItem<T>> = emptyList(),
        val isSearchFieldVisible: Boolean = false,
        val searchQuery: String? = null,
    ) : State

}
