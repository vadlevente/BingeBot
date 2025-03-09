package com.vadlevente.bingebot.bottomsheet.viewmodel.tv

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.bottomsheet.domain.usecases.CreateWatchListUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.GetItemWatchListsUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.GetWatchListsUseCaseParams
import com.vadlevente.bingebot.bottomsheet.viewmodel.ItemWatchListsBottomSheetViewModel
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowWatchListsBottomSheet.ShowTvWatchListsBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEventChannel
import com.vadlevente.bingebot.core.events.dialog.DialogEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.Item.Tv
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TvWatchListsBottomSheetViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    bottomSheetEventChannel: BottomSheetEventChannel,
    getWatchListsUseCase: GetItemWatchListsUseCase<Tv>,
    dialogEventChannel: DialogEventChannel,
    createWatchListUseCase: CreateWatchListUseCase<Tv>,
) : ItemWatchListsBottomSheetViewModel<Tv>(
    navigationEventChannel, toastEventChannel, dialogEventChannel, createWatchListUseCase
) {

    init {
        bottomSheetEventChannel.events.filterIsInstance<ShowTvWatchListsBottomSheet>()
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

    override fun onWatchListSelected(watchListId: String) {
        navigateTo(TV_WATCH_LIST, watchListId)
        onDismiss()
    }

}