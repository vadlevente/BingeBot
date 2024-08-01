package com.vadlevente.bingebot.search

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowItemBottomSheet.ShowTvBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.Item.Tv
import com.vadlevente.bingebot.search.usecase.GetSearchResultUseCase
import com.vadlevente.bingebot.search.usecase.SearchItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchTvViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    getSearchResultUseCase: GetSearchResultUseCase<Tv>,
    searchItemUseCase: SearchItemUseCase<Tv>,
    private val bottomSheetEventChannel: BottomSheetEventChannel,
) : SearchItemViewModel<Tv>(
    navigationEventChannel, toastEventChannel, getSearchResultUseCase, searchItemUseCase, bottomSheetEventChannel
) {

    override fun onNavigateToOptions(itemId: Int) {
        viewState.value.items.firstOrNull { it.item.id == itemId }?.let {
            viewModelScope.launch {
                bottomSheetEventChannel.sendEvent(
                    ShowTvBottomSheet(
                        item = it,
                        alreadySaved = false,
                    )
                )
            }
        }
    }

}