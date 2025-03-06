package com.vadlevente.bingebot.bottomsheet.viewmodel.movie

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.bottomsheet.domain.usecases.GetOrderByListUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.SetOrderByFilterUseCase
import com.vadlevente.bingebot.bottomsheet.viewmodel.OrderByBottomSheetViewModel
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowOrderByBottomSheet.ShowMovieOrderByBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.Item.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MovieOrderByBottomSheetViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    getOrderByListUseCase: GetOrderByListUseCase<Movie>,
    setOrderByFilterUseCase: SetOrderByFilterUseCase<Movie>,
    bottomSheetEventChannel: BottomSheetEventChannel,
) : OrderByBottomSheetViewModel<Movie>(
    navigationEventChannel, toastEventChannel, getOrderByListUseCase, setOrderByFilterUseCase
) {

    init {
        bottomSheetEventChannel.events.filterIsInstance<ShowMovieOrderByBottomSheet>().onEach { event ->
            viewState.update {
                it.copy(
                    isVisible = true,
                )
            }
        }.launchIn(viewModelScope)
    }

}