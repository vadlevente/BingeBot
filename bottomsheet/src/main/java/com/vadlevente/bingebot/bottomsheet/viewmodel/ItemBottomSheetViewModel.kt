package com.vadlevente.bingebot.bottomsheet.viewmodel

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.bottomsheet.R
import com.vadlevente.bingebot.bottomsheet.domain.usecases.ItemBottomSheetUseCases
import com.vadlevente.bingebot.bottomsheet.domain.usecases.movie.DeleteItemUseCaseParams
import com.vadlevente.bingebot.bottomsheet.domain.usecases.movie.RemoveItemFromWatchListUseCaseParams
import com.vadlevente.bingebot.bottomsheet.domain.usecases.movie.SaveItemUseCaseParams
import com.vadlevente.bingebot.bottomsheet.domain.usecases.movie.SetItemSeenUseCaseParams
import com.vadlevente.bingebot.bottomsheet.viewmodel.ItemBottomSheetViewModel.ViewState
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowItemBottomSheet
import com.vadlevente.bingebot.core.events.dialog.DialogEvent.ShowDialog
import com.vadlevente.bingebot.core.events.dialog.DialogEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastType.INFO
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import com.vadlevente.bingebot.resources.R as Res

abstract class ItemBottomSheetViewModel <T: Item> (
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    private val dialogEventChannel: DialogEventChannel,
    private val useCases: ItemBottomSheetUseCases<T>,
) : BaseViewModel<ViewState<T>>(
    navigationEventChannel, toastEventChannel
) {

    protected val viewState = MutableStateFlow(ViewState<T>())
    override val state: StateFlow<ViewState<T>> = viewState

    abstract val stringResources: StringResources

    abstract fun onAddToWatchList()
    abstract fun onShowDetails()

    fun onDismiss() {
        viewState.update {
            it.copy(
                isVisible = false,
                event = null,
            )
        }
    }

    fun onSaveItem(item: T) {
        useCases.saveItemUseCase.execute(SaveItemUseCaseParams(item))
            .onValue {
                showToast(
                    stringOf(stringResources.saveSuccessfulToast),
                    INFO,
                )
                navigateUp()
                onDismiss()
            }
    }

    fun removeFromWatchList() {
        viewState.value.event?.watchListId?.let { watchListId ->
            viewState.value.event?.item?.let { item ->
                viewModelScope.launch {
                    dialogEventChannel.sendEvent(
                        ShowDialog(
                            title = stringOf(stringResources.removeFromWatchListTitle),
                            content = stringOf(stringResources.removeFromWatchListDescription),
                            positiveButtonTitle = stringOf(Res.string.common_Yes),
                            negativeButtonTitle = stringOf(Res.string.common_No),
                            onPositiveButtonClicked = {
                                useCases.removeItemFromWatchListUseCase.execute(
                                    RemoveItemFromWatchListUseCaseParams(
                                        itemId = item.item.id,
                                        watchListId = watchListId,
                                    )
                                )
                                    .onValue {
                                        onDismiss()
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
        }
    }

    fun onDelete() {
        val itemId = viewState.value.event?.item?.item?.id ?: return
        viewModelScope.launch {
            dialogEventChannel.sendEvent(
                ShowDialog(
                    title = stringOf(stringResources.deleteTitle),
                    content = stringOf(stringResources.deleteDescription),
                    positiveButtonTitle = stringOf(Res.string.common_Yes),
                    negativeButtonTitle = stringOf(Res.string.common_No),
                    onPositiveButtonClicked = {
                        useCases.deleteItemUseCase.execute(
                            DeleteItemUseCaseParams(itemId)
                        )
                            .onValue {
                                onDismiss()
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

    fun onSetItemWatched(dateMillis: Long) {
        viewState.value.event?.let {
            useCases.setItemSeenUseCase.execute(
                SetItemSeenUseCaseParams(
                    itemId = it.item.item.id,
                    watchedDate = Date(dateMillis),
                )
            ).onValue {
                onDismiss()
                showToast(
                    message = stringOf(R.string.movieBottomSheet_setWatchedDateSuccessful),
                    type = INFO,
                )
            }
        }
    }

    fun onSetItemNotWatched() {
        viewState.value.event?.let {
            useCases.setItemSeenUseCase.execute(
                SetItemSeenUseCaseParams(
                    itemId = it.item.item.id,
                    watchedDate = null,
                )
            ).onValue {
                onDismiss()
                showToast(
                    message = stringOf(R.string.movieBottomSheet_revertWatchedDateSuccessful),
                    type = INFO,
                )
            }
        }
    }


    data class ViewState<T: Item>(
        val isVisible: Boolean = false,
        val event: ShowItemBottomSheet<T>? = null,
    ) : State

    data class StringResources(
        val saveSuccessfulToast: Int,
        val removeFromWatchListTitle: Int,
        val removeFromWatchListDescription: Int,
        val deleteTitle: Int,
        val deleteDescription: Int,
    )

}