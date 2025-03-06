package com.vadlevente.bingebot.bottomsheet.viewmodel.tv

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.bottomsheet.domain.usecases.GetOrderByListUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.SetOrderByFilterUseCase
import com.vadlevente.bingebot.bottomsheet.viewmodel.OrderByBottomSheetViewModel
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowOrderByBottomSheet.ShowTvOrderByBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEventChannel
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
class TvOrderByBottomSheetViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    getOrderByListUseCase: GetOrderByListUseCase<Tv>,
    setOrderByFilterUseCase: SetOrderByFilterUseCase<Tv>,
    bottomSheetEventChannel: BottomSheetEventChannel,
) : OrderByBottomSheetViewModel<Tv>(
    navigationEventChannel, toastEventChannel, getOrderByListUseCase, setOrderByFilterUseCase
) {

    init {
        bottomSheetEventChannel.events.filterIsInstance<ShowTvOrderByBottomSheet>().onEach { event ->
            viewState.update {
                it.copy(
                    isVisible = true,
                )
            }
        }.launchIn(viewModelScope)
    }

}