package com.vadlevente.bingebot.watchlist.viewmodel.tv

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowItemBottomSheet.ShowTvBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEventChannel
import com.vadlevente.bingebot.core.events.dialog.DialogEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.Item.Tv
import com.vadlevente.bingebot.core.model.NavDestination.SEARCH_TV
import com.vadlevente.bingebot.watchlist.domain.usecase.DeleteWatchListUseCase
import com.vadlevente.bingebot.watchlist.domain.usecase.GetWatchListItemsUseCase
import com.vadlevente.bingebot.watchlist.domain.usecase.GetWatchListUseCase
import com.vadlevente.bingebot.watchlist.viewmodel.ItemWatchListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvWatchListViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    getWatchListItemsUseCase: GetWatchListItemsUseCase<Tv>,
    getWatchListUseCase: GetWatchListUseCase<Tv>,
    deleteWatchListUseCase: DeleteWatchListUseCase<Tv>,
    dialogEventChannel: DialogEventChannel,
    private val bottomSheetEventChannel: BottomSheetEventChannel,
) : ItemWatchListViewModel<Tv>(
    navigationEventChannel, toastEventChannel, getWatchListItemsUseCase, getWatchListUseCase, deleteWatchListUseCase, dialogEventChannel
) {

    override fun onNavigateToSearch() {
        navigateTo(SEARCH_TV)
    }

    override fun onNavigateToOptions(itemId: Int) {
        viewState.value.items.firstOrNull { it.item.id == itemId }?.let {
            viewModelScope.launch {
                bottomSheetEventChannel.sendEvent(
                    ShowTvBottomSheet(
                        item = it,
                        alreadySaved = true,
                        watchListId = watchListId,
                    )
                )
            }
        }
    }

}
