package com.vadlevente.bingebot.watchlist.viewmodel.movie

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowItemBottomSheet.ShowMovieBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEventChannel
import com.vadlevente.bingebot.core.events.dialog.DialogEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.Item.Movie
import com.vadlevente.bingebot.core.model.NavDestination
import com.vadlevente.bingebot.watchlist.domain.usecase.DeleteWatchListUseCase
import com.vadlevente.bingebot.watchlist.domain.usecase.GetWatchListItemsUseCase
import com.vadlevente.bingebot.watchlist.domain.usecase.GetWatchListUseCase
import com.vadlevente.bingebot.watchlist.viewmodel.ItemWatchListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieWatchListViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    getWatchListItemsUseCase: GetWatchListItemsUseCase<Movie>,
    getWatchListUseCase: GetWatchListUseCase<Movie>,
    deleteWatchListUseCase: DeleteWatchListUseCase<Movie>,
    dialogEventChannel: DialogEventChannel,
    private val bottomSheetEventChannel: BottomSheetEventChannel,
) : ItemWatchListViewModel<Movie>(
    navigationEventChannel, toastEventChannel, getWatchListItemsUseCase, getWatchListUseCase, deleteWatchListUseCase, dialogEventChannel
) {

    override fun onNavigateToSearch() {
        navigateTo(NavDestination.SearchMovie)
    }

    override fun onNavigateToOptions(itemId: Int) {
        viewState.value.items.firstOrNull { it.item.id == itemId }?.let {
            viewModelScope.launch {
                bottomSheetEventChannel.sendEvent(
                    ShowMovieBottomSheet(
                        item = it,
                        alreadySaved = true,
                        watchListId = watchListId,
                    )
                )
            }
        }
    }

}
