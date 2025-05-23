package com.vadlevente.bingebot.search

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowItemBottomSheet.ShowMovieBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.Item.Movie
import com.vadlevente.bingebot.search.usecase.GetSearchResultUseCase
import com.vadlevente.bingebot.search.usecase.SearchItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchMovieViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    getSearchResultUseCase: GetSearchResultUseCase<Movie>,
    searchItemUseCase: SearchItemUseCase<Movie>,
    private val bottomSheetEventChannel: BottomSheetEventChannel,
) : SearchItemViewModel<Movie>(
    navigationEventChannel, toastEventChannel, getSearchResultUseCase, searchItemUseCase, bottomSheetEventChannel
) {

    override fun onNavigateToOptions(itemId: Int) {
        viewState.value.items.firstOrNull { it.item.id == itemId }?.let {
            viewModelScope.launch {
                bottomSheetEventChannel.sendEvent(
                    ShowMovieBottomSheet(
                        item = it,
                        alreadySaved = false,
                    )
                )
            }
        }
    }

}