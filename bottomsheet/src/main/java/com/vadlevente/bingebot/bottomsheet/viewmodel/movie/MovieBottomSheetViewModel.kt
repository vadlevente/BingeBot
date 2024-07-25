package com.vadlevente.bingebot.bottomsheet.viewmodel.movie

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.bottomsheet.R.string
import com.vadlevente.bingebot.bottomsheet.domain.usecases.ItemBottomSheetUseCases
import com.vadlevente.bingebot.bottomsheet.viewmodel.ItemBottomSheetViewModel
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowAddItemToWatchListBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowItemBottomSheet.ShowMovieBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEventChannel
import com.vadlevente.bingebot.core.events.dialog.DialogEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.Item.Movie
import com.vadlevente.bingebot.core.model.NavDestination.MOVIE_DETAILS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieBottomSheetViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    useCases: ItemBottomSheetUseCases<Movie>,
    dialogEventChannel: DialogEventChannel,
    private val bottomSheetEventChannel: BottomSheetEventChannel,
) : ItemBottomSheetViewModel<Movie>(
    navigationEventChannel, toastEventChannel, dialogEventChannel, useCases
) {

    override val stringResources = StringResources(
        saveSuccessfulToast = string.movieBottomSheet_saveSuccessful,
        removeFromWatchListTitle = string.movieBottomSheet_removeMovieFromWatchListConfirmationTitle,
        removeFromWatchListDescription = string.movieBottomSheet_removeMovieFromWatchListConfirmationDescription,
        deleteTitle = string.movieBottomSheet_deleteConfirmationTitle,
        deleteDescription = string.movieBottomSheet_deleteConfirmationDescription
    )

    init {
        bottomSheetEventChannel.events.filterIsInstance<ShowMovieBottomSheet>().onEach { event ->
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
            val displayedMovie = event.item
            viewModelScope.launch {
                bottomSheetEventChannel.sendEvent(
                    ShowAddItemToWatchListBottomSheet(
                        movie = displayedMovie,
                        alreadySaved = event.alreadySaved,
                    )
                )
            }
            onDismiss()
        }
    }

    override fun onShowDetails() {
        viewState.value.event?.let { event ->
            navigateTo(MOVIE_DETAILS, event.item.item.id)
            onDismiss()
        }
    }

}