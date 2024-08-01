package com.vadlevente.bingebot.bottomsheet.viewmodel.tv

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.bottomsheet.R
import com.vadlevente.bingebot.bottomsheet.domain.usecases.movie.AddItemToWatchListUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.movie.CreateMovieWatchListUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.movie.GetMovieWatchListsUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.movie.GetWatchListsUseCaseParams
import com.vadlevente.bingebot.bottomsheet.domain.usecases.movie.SaveItemUseCase
import com.vadlevente.bingebot.bottomsheet.viewmodel.AddItemToWatchListBottomSheetViewModel
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowAddItemToWatchListBottomSheet.ShowAddTvToWatchListBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEventChannel
import com.vadlevente.bingebot.core.events.dialog.DialogEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastType.INFO
import com.vadlevente.bingebot.core.model.Item.Tv
import com.vadlevente.bingebot.core.stringOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddTvToWatchListBottomSheetViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    bottomSheetEventChannel: BottomSheetEventChannel,
    getWatchListsUseCase: GetMovieWatchListsUseCase<Tv>,
    dialogEventChannel: DialogEventChannel,
    createWatchListUseCase: CreateMovieWatchListUseCase<Tv>,
    addItemToWatchListUseCase: AddItemToWatchListUseCase<Tv>,
    saveItemUseCase: SaveItemUseCase<Tv>,
) : AddItemToWatchListBottomSheetViewModel<Tv>(
    navigationEventChannel, toastEventChannel, dialogEventChannel, createWatchListUseCase, addItemToWatchListUseCase, saveItemUseCase
) {

    init {
        bottomSheetEventChannel.events.filterIsInstance<ShowAddTvToWatchListBottomSheet>()
            .onEach { event ->
                viewState.update {
                    it.copy(
                        isVisible = true,
                        event = event,
                    )
                }
                getWatchListsUseCase.execute(
                    GetWatchListsUseCaseParams(event.item.item.id)
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

    override fun showAddedToast(isNewlyAdded: Boolean) {
        showToast(
            message = stringOf(
                if (isNewlyAdded) {
                    R.string.addItemToWatchListBottomSheet_itemAddedToWatchList
                } else R.string.addTvToWatchListBottomSheet_itemAlreadyOnWatchList
            ),
            type = INFO,
        )
    }

}