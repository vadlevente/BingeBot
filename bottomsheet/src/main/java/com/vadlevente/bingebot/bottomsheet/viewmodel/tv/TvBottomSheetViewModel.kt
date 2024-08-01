package com.vadlevente.bingebot.bottomsheet.viewmodel.tv

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.bottomsheet.R
import com.vadlevente.bingebot.bottomsheet.domain.usecases.ItemBottomSheetUseCases
import com.vadlevente.bingebot.bottomsheet.viewmodel.ItemBottomSheetViewModel
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowAddItemToWatchListBottomSheet.ShowAddTvToWatchListBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowItemBottomSheet.ShowTvBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEventChannel
import com.vadlevente.bingebot.core.events.dialog.DialogEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.Item.Tv
import com.vadlevente.bingebot.core.model.NavDestination.TV_DETAILS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvBottomSheetViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    useCases: ItemBottomSheetUseCases<Tv>,
    dialogEventChannel: DialogEventChannel,
    private val bottomSheetEventChannel: BottomSheetEventChannel,
) : ItemBottomSheetViewModel<Tv>(
    navigationEventChannel, toastEventChannel, dialogEventChannel, useCases
) {

    override val stringResources = StringResources(
        saveSuccessfulToast = R.string.tvBottomSheet_saveSuccessful,
        removeFromWatchListTitle = R.string.tvBottomSheet_removeItemFromWatchListConfirmationTitle,
        removeFromWatchListDescription = R.string.tvBottomSheet_removeItemFromWatchListConfirmationDescription,
        deleteTitle = R.string.tvBottomSheet_deleteConfirmationTitle,
        deleteDescription = R.string.tvBottomSheet_deleteConfirmationDescription
    )

    init {
        bottomSheetEventChannel.events.filterIsInstance<ShowTvBottomSheet>().onEach { event ->
            viewState.update {
                it.copy(
                    isVisible = true,
                    event = event,
                )
            }
        }.launchIn(viewModelScope)
    }

    override fun onAddToWatchList() {
        viewState.value.event?.let { event ->
            val displayedTv = event.item
            viewModelScope.launch {
                bottomSheetEventChannel.sendEvent(
                    ShowAddTvToWatchListBottomSheet(
                        item = displayedTv,
                        alreadySaved = event.alreadySaved,
                    )
                )
            }
            onDismiss()
        }
    }

    override fun onShowDetails() {
        viewState.value.event?.let { event ->
            navigateTo(TV_DETAILS, event.item.item.id)
            onDismiss()
        }
    }

}