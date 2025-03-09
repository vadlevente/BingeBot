package com.vadlevente.moviedetails

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowItemBottomSheet.ShowTvBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.Item.Tv
import com.vadlevente.bingebot.core.model.SkeletonFactory
import com.vadlevente.moviedetails.domain.usecases.GetItemDetailsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel(assistedFactory = TvDetailsViewModel.TvDetailsViewModelFactory::class)
class TvDetailsViewModel  @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    getItemDetailsUseCase: GetItemDetailsUseCase<Tv>,
    private val bottomSheetEventChannel: BottomSheetEventChannel,
    @Assisted val id: Int,
) : ItemDetailsViewModel<Tv>(
    navigationEventChannel, toastEventChannel, getItemDetailsUseCase, id
) {

    override val skeletonFactory: SkeletonFactory<Tv>
        get() = SkeletonFactory.TvSkeletonFactory

    override fun onNavigateToOptions() {
        baseViewState.value.details?.let {
                viewModelScope.launch {
                    bottomSheetEventChannel.sendEvent(
                        ShowTvBottomSheet(
                            item = it.displayedItem,
                            alreadySaved = it.displayedItem.item.createdDate != null,
                        )
                    )
                }
            }
    }

    @AssistedFactory
    interface TvDetailsViewModelFactory {
        fun create(
            id: Int,
        ): TvDetailsViewModel
    }

}