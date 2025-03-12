package com.vadlevente.bingebot.list

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowItemBottomSheet.ShowTvBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowOrderByBottomSheet.ShowTvOrderByBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowWatchListsBottomSheet.ShowTvWatchListsBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEvent
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.Item.Tv
import com.vadlevente.bingebot.core.model.NavDestination.AuthenticatedNavDestination
import com.vadlevente.bingebot.core.model.SkeletonFactory
import com.vadlevente.bingebot.list.domain.usecase.ItemListUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvListViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    useCases: ItemListUseCases<Tv>,
    private val bottomSheetEventChannel: BottomSheetEventChannel,
) : ItemListViewModel<Tv>(
    navigationEventChannel, toastEventChannel, useCases
) {

    override val skeletonFactory: SkeletonFactory<Tv>
        get() = SkeletonFactory.TvSkeletonFactory

    override fun onNavigateToSearch() {
        viewModelScope.launch {
            navigationEventChannel.sendEvent(
                NavigationEvent.AuthenticatedNavigationEvent.NavigateTo(AuthenticatedNavDestination.SearchTv)
            )
        }
    }

    override fun onNavigateToOptions(itemId: Int) {
        baseViewState.value.items
            .firstOrNull { it.item.id == itemId }
            ?.let {
                viewModelScope.launch {
                    bottomSheetEventChannel.sendEvent(
                        ShowTvBottomSheet(
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
                ShowTvWatchListsBottomSheet
            )
        }
    }

    override fun onOpenOrderBy() {
        viewModelScope.launch {
            bottomSheetEventChannel.sendEvent(
                ShowTvOrderByBottomSheet
            )
        }
    }

}