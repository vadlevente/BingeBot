package com.vadlevente.bingebot.list

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.core.delegates.AppCloserDelegate
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowItemBottomSheet.ShowMovieBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowOrderByBottomSheet.ShowMovieOrderByBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowWatchListsBottomSheet.ShowMovieWatchListsBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.Item.Movie
import com.vadlevente.bingebot.core.model.NavDestination
import com.vadlevente.bingebot.core.model.SkeletonFactory
import com.vadlevente.bingebot.list.domain.usecase.ItemListUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    useCases: ItemListUseCases<Movie>,
    private val bottomSheetEventChannel: BottomSheetEventChannel,
    private val appCloserDelegate: AppCloserDelegate,
) : ItemListViewModel<Movie>(
    navigationEventChannel, toastEventChannel, useCases
), AppCloserDelegate by appCloserDelegate {

    override val skeletonFactory: SkeletonFactory<Movie>
        get() = SkeletonFactory.MovieSkeletonFactory

    override fun onNavigateToSearch() {
        navigateTo(NavDestination.SearchMovie)
    }

    override fun onNavigateToOptions(itemId: Int) {
        baseViewState.value.items
            .firstOrNull { it.item.id == itemId }
            ?.let {
                viewModelScope.launch {
                    bottomSheetEventChannel.sendEvent(
                        ShowMovieBottomSheet(
                            item = it,
                            alreadySaved = true,
                        )
                    )
                }
            }
    }

    override fun onOpenWatchLists() {
        viewModelScope.launch {
            bottomSheetEventChannel.sendEvent(
                ShowMovieWatchListsBottomSheet
            )
        }
    }

    override fun onOpenOrderBy() {
        viewModelScope.launch {
            bottomSheetEventChannel.sendEvent(
                ShowMovieOrderByBottomSheet
            )
        }
    }

}