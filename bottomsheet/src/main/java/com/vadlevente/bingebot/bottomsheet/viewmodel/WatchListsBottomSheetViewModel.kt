package com.vadlevente.bingebot.bottomsheet.viewmodel

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.bottomsheet.R
import com.vadlevente.bingebot.bottomsheet.domain.usecases.CreateWatchListUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.CreateWatchListUseCaseParams
import com.vadlevente.bingebot.bottomsheet.domain.usecases.GetWatchListsUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.GetWatchListsUseCaseParams
import com.vadlevente.bingebot.bottomsheet.viewmodel.WatchListsBottomSheetViewModel.ViewState
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowWatchListsBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEventChannel
import com.vadlevente.bingebot.core.events.dialog.DialogEvent.ShowTextFieldDialog
import com.vadlevente.bingebot.core.events.dialog.DialogEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.NavDestination.WATCH_LIST
import com.vadlevente.bingebot.core.model.WatchList
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.vadlevente.bingebot.resources.R as Res

@HiltViewModel
class WatchListsBottomSheetViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    bottomSheetEventChannel: BottomSheetEventChannel,
    getWatchListsUseCase: GetWatchListsUseCase,
    private val dialogEventChannel: DialogEventChannel,
    private val createWatchListUseCase: CreateWatchListUseCase,
) : BaseViewModel<ViewState>(
    navigationEventChannel, toastEventChannel
) {

    private val viewState = MutableStateFlow(ViewState())
    override val state: StateFlow<ViewState> = viewState

    init {
        bottomSheetEventChannel.events.filterIsInstance<ShowWatchListsBottomSheet>()
            .onEach {
                viewState.update {
                    it.copy(
                        isVisible = true,
                    )
                }
                getWatchListsUseCase.execute(
                    GetWatchListsUseCaseParams()
                )
                    .onValue { watchLists ->
                        viewState.update {
                            it.copy(
                                watchLists = watchLists,
                            )
                        }
                    }
            }.launchIn(viewModelScope)

    }

    fun onDismiss() {
        viewState.update {
            it.copy(
                isVisible = false,
            )
        }
    }

    fun onWatchListSelected(watchListId: String) {
        navigateTo(WATCH_LIST, watchListId)
        onDismiss()
    }

    fun onCreateWatchList() {
        viewModelScope.launch {
            dialogEventChannel.sendEvent(
                ShowTextFieldDialog(
                    title = stringOf(R.string.addMovieToWatchListBottomSheet_createWatchListDialogTitle),
                    content = stringOf(R.string.addMovieToWatchListBottomSheet_createWatchListDialogDescription),
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