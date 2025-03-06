package com.vadlevente.bingebot.bottomsheet.viewmodel

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.bottomsheet.R
import com.vadlevente.bingebot.bottomsheet.domain.usecases.CreateWatchListUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.CreateWatchListUseCaseParams
import com.vadlevente.bingebot.bottomsheet.viewmodel.ItemWatchListsBottomSheetViewModel.ViewState
import com.vadlevente.bingebot.core.events.dialog.DialogEvent.ShowTextFieldDialog
import com.vadlevente.bingebot.core.events.dialog.DialogEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.model.WatchList
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.vadlevente.bingebot.resources.R as Res

abstract class ItemWatchListsBottomSheetViewModel <T : Item>(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    private val dialogEventChannel: DialogEventChannel,
    private val createWatchListUseCase: CreateWatchListUseCase<T>,
) : BaseViewModel<ViewState>(
    navigationEventChannel, toastEventChannel
) {

    protected val viewState = MutableStateFlow(ViewState())
    override val state: StateFlow<ViewState> = viewState
    abstract fun onWatchListSelected(watchListId: String)

    fun onDismiss() {
        viewState.update {
            it.copy(
                isVisible = false,
            )
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
                            .onStart()
                    },
                )
            )
        }
    }

    data class ViewState(
        val isVisible: Boolean = false,
        val watchLists: List<WatchList> = emptyList(),
    ) : State

}