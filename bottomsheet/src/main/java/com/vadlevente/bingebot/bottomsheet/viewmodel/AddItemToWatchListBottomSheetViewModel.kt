package com.vadlevente.bingebot.bottomsheet.viewmodel

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.bottomsheet.R
import com.vadlevente.bingebot.bottomsheet.domain.usecases.AddItemToWatchListUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.AddItemToWatchListUseCaseParams
import com.vadlevente.bingebot.bottomsheet.domain.usecases.CreateWatchListUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.CreateWatchListUseCaseParams
import com.vadlevente.bingebot.bottomsheet.domain.usecases.SaveItemUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.SaveItemUseCaseParams
import com.vadlevente.bingebot.bottomsheet.viewmodel.AddItemToWatchListBottomSheetViewModel.ViewState
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowAddItemToWatchListBottomSheet
import com.vadlevente.bingebot.core.events.dialog.DialogEvent.ShowTextFieldDialog
import com.vadlevente.bingebot.core.events.dialog.DialogEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.model.WatchList
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.vadlevente.bingebot.resources.R as Res

abstract class AddItemToWatchListBottomSheetViewModel <T : Item>(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    private val dialogEventChannel: DialogEventChannel,
    private val createWatchListUseCase: CreateWatchListUseCase<T>,
    private val addItemToWatchListUseCase: AddItemToWatchListUseCase<T>,
    private val saveItemUseCase: SaveItemUseCase<T>,
) : BaseViewModel<ViewState<T>>(
    navigationEventChannel, toastEventChannel
) {

    protected val viewState = MutableStateFlow(ViewState<T>())
    override val state: StateFlow<ViewState<T>> = viewState

    abstract fun showAddedToast(isNewlyAdded: Boolean)

    fun onDismiss() {
        viewState.update {
            it.copy(
                isVisible = false,
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun onAddToWatchList(watchListId: String) {
        viewState.value.event?.let { event ->
            val startFlow = if (event.alreadySaved) {
                flowOf(Unit)
            } else saveItemUseCase.execute(SaveItemUseCaseParams(event.item.item))
            startFlow.flatMapConcat {
                addItemToWatchListUseCase.execute(
                    AddItemToWatchListUseCaseParams(
                        event.item.item.id, watchListId,
                    )
                )
            }
                .onValue { isNewlyAdded ->
                    onDismiss()
                    showAddedToast(isNewlyAdded)
                }
        }
    }

    fun onCreateWatchList() {
        viewModelScope.launch {
            dialogEventChannel.sendEvent(
                ShowTextFieldDialog(
                    title = stringOf(R.string.addItemToWatchListBottomSheet_createWatchListDialogTitle),
                    content = stringOf(R.string.addItemToWatchListBottomSheet_createWatchListDialogDescription),
                    positiveButtonTitle = stringOf(Res.string.common_Create),
                    negativeButtonTitle = stringOf(Res.string.common_Cancel),
                    onPositiveButtonClicked = { title ->
                        createWatchListUseCase.execute(
                            CreateWatchListUseCaseParams(title)
                        )
                            .onValue {
                                onAddToWatchList(it)
                            }
                    },
                )
            )
        }
    }

    data class ViewState<T: Item>(
        val isVisible: Boolean = false,
        val event: ShowAddItemToWatchListBottomSheet<T>? = null,
        val watchLists: List<WatchList> = emptyList(),
    ) : State

}