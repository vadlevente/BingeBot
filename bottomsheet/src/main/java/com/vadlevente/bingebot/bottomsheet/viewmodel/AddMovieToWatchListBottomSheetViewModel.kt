package com.vadlevente.bingebot.bottomsheet.viewmodel

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.bottomsheet.R
import com.vadlevente.bingebot.bottomsheet.domain.usecases.movie.AddItemToWatchListUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.movie.AddItemToWatchListUseCaseParams
import com.vadlevente.bingebot.bottomsheet.domain.usecases.movie.CreateMovieWatchListUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.movie.CreateWatchListUseCaseParams
import com.vadlevente.bingebot.bottomsheet.domain.usecases.movie.GetMovieWatchListsUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.movie.GetWatchListsUseCaseParams
import com.vadlevente.bingebot.bottomsheet.domain.usecases.movie.SaveItemUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.movie.SaveItemUseCaseParams
import com.vadlevente.bingebot.bottomsheet.viewmodel.AddMovieToWatchListBottomSheetViewModel.ViewState
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowAddItemToWatchListBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEventChannel
import com.vadlevente.bingebot.core.events.dialog.DialogEvent.ShowTextFieldDialog
import com.vadlevente.bingebot.core.events.dialog.DialogEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastType.INFO
import com.vadlevente.bingebot.core.model.Item.Movie
import com.vadlevente.bingebot.core.model.WatchList
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.vadlevente.bingebot.resources.R as Res

@HiltViewModel
class AddMovieToWatchListBottomSheetViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    bottomSheetEventChannel: BottomSheetEventChannel,
    getWatchListsUseCase: GetMovieWatchListsUseCase<Movie>,
    private val dialogEventChannel: DialogEventChannel,
    private val createWatchListUseCase: CreateMovieWatchListUseCase<Movie>,
    private val addItemToWatchListUseCase: AddItemToWatchListUseCase<Movie>,
    private val saveItemUseCase: SaveItemUseCase<Movie>,
) : BaseViewModel<ViewState>(
    navigationEventChannel, toastEventChannel
) {

    private val viewState = MutableStateFlow(ViewState())
    override val state: StateFlow<ViewState> = viewState

    init {
        bottomSheetEventChannel.events.filterIsInstance<ShowAddItemToWatchListBottomSheet>()
            .onEach { event ->
                viewState.update {
                    it.copy(
                        isVisible = true,
                        event = event,
                    )
                }
                getWatchListsUseCase.execute(
                    GetWatchListsUseCaseParams(event.movie.item.id)
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

    @OptIn(FlowPreview::class)
    fun onAddToWatchList(watchListId: String) {
        viewState.value.event?.let { event ->
            val startFlow = if (event.alreadySaved) {
                flowOf(Unit)
            } else saveItemUseCase.execute(SaveItemUseCaseParams(event.movie.item))
            startFlow.flatMapConcat {
                addItemToWatchListUseCase.execute(
                    AddItemToWatchListUseCaseParams(
                        event.movie.item.id, watchListId,
                    )
                )
            }
                .onValue { isNewlyAdded ->
                    onDismiss()

                    showToast(
                        message = stringOf(
                            if (isNewlyAdded) {
                                R.string.addMovieToWatchListBottomSheet_movieAddedToWatchList
                            } else R.string.addMovieToWatchListBottomSheet_movieAlreadyOnWatchList
                        ),
                        type = INFO,
                    )
                }
        }
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
                            .onValue {
                                onAddToWatchList(it)
                            }
                    },
                )
            )
        }
    }

    data class ViewState(
        val isVisible: Boolean = false,
        val event: ShowAddItemToWatchListBottomSheet? = null,
        val watchLists: List<WatchList> = emptyList(),
    ) : State

}