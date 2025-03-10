package com.vadlevente.moviedetails

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowItemBottomSheet.ShowMovieBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.Item.Movie
import com.vadlevente.moviedetails.domain.usecases.GetItemDetailsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = MovieDetailsViewModel.MovieDetailsViewModelFactory::class)
class MovieDetailsViewModel  @AssistedInject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    getItemDetailsUseCase: GetItemDetailsUseCase<Movie>,
    private val bottomSheetEventChannel: BottomSheetEventChannel,
    @Assisted val id: Int,
) : ItemDetailsViewModel<Movie>(
    navigationEventChannel, toastEventChannel, getItemDetailsUseCase, id
) {

    override fun onNavigateToOptions() {
        baseViewState.value.details?.let {
                viewModelScope.launch {
                    bottomSheetEventChannel.sendEvent(
                        ShowMovieBottomSheet(
                            item = it.displayedItem,
                            alreadySaved = it.displayedItem.item.createdDate != null,
                            openedFromDetail = true,
                        )
                    )
                }
            }
    }

    @AssistedFactory
    interface MovieDetailsViewModelFactory {
        fun create(
            id: Int,
        ): MovieDetailsViewModel
    }

}