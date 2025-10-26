package com.vadlevente.moviedetails

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.core.events.navigation.NavigationEvent
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.MediaType
import com.vadlevente.bingebot.core.model.NavDestination
import com.vadlevente.bingebot.core.model.PersonDetails
import com.vadlevente.bingebot.core.util.Constants.LOADING_DELAY_MS
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.State
import com.vadlevente.moviedetails.PersonDetailsViewModel.ViewState
import com.vadlevente.moviedetails.domain.usecases.GetPersonDetailsUseCase
import com.vadlevente.moviedetails.domain.usecases.GetPersonDetailsUseCaseParams
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = PersonDetailsViewModel.PersonDetailsViewModelFactory::class)
class PersonDetailsViewModel @AssistedInject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    getPersonDetailsUseCase: GetPersonDetailsUseCase,
    @Assisted private val personId: Int,
) : BaseViewModel<ViewState>(
    navigationEventChannel, toastEventChannel
) {

    val baseViewState = MutableStateFlow(ViewState())
    override val state = baseViewState.asStateFlow()

    init {
        getPersonDetailsUseCase.execute(
            GetPersonDetailsUseCaseParams(
                personId = personId
            )
        ).onValue { details ->
            isInProgress.value = true
            baseViewState.update {
                it.copy(
                    details = details
                )
            }
            delay(LOADING_DELAY_MS)
            isInProgress.value = false
        }
    }

    fun onBackPressed() {
        viewModelScope.launch {
            navigationEventChannel.sendEvent(
                NavigationEvent.AuthenticatedNavigationEvent.NavigateUp
            )
        }
    }

    fun onMediaClicked(mediaId: Int, mediaType: MediaType) {
        viewModelScope.launch {
            navigationEventChannel.sendEvent(
                NavigationEvent.AuthenticatedNavigationEvent.NavigateTo(
                    when (mediaType) {
                        MediaType.Movie -> NavDestination.AuthenticatedNavDestination.MovieDetails(mediaId)
                        MediaType.Tv -> NavDestination.AuthenticatedNavDestination.TvDetails(mediaId)
                    }
                )
            )
        }
    }

    data class ViewState(
        val details: PersonDetails? = null,
    ) : State

    @AssistedFactory
    interface PersonDetailsViewModelFactory {
        fun create(
            personId: Int,
        ): PersonDetailsViewModel
    }

}