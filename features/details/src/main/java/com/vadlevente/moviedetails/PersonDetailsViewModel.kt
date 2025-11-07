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
import com.vadlevente.moviedetails.domain.model.DisplayedPersonDetails
import com.vadlevente.moviedetails.domain.usecases.GetDisplayedPersonDetailsUseCase
import com.vadlevente.moviedetails.domain.usecases.GetDisplayedPersonDetailsUseCaseParams
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
    private val getDisplayedPersonDetailsUseCase: GetDisplayedPersonDetailsUseCase,
    @Assisted private val personId: Int,
) : BaseViewModel<ViewState>(
    navigationEventChannel, toastEventChannel
) {

    val baseViewState = MutableStateFlow(ViewState())
    override val state = baseViewState.asStateFlow()

    private var details: PersonDetails? = null

    init {
        getPersonDetailsUseCase.execute(
            GetPersonDetailsUseCaseParams(
                personId = personId
            )
        ).onValue { details ->
            isInProgress.value = true
            this.details = details
            updateDisplayedDetails()
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

    fun toggleCastUpcomingHidden() {
        baseViewState.update {
            it.copy(
                castUpcomingHidden = !it.castUpcomingHidden
            )
        }
        updateDisplayedDetails()
    }

    fun toggleCastPreviousHidden() {
        baseViewState.update {
            it.copy(
                castPreviousHidden = !it.castPreviousHidden
            )
        }
        updateDisplayedDetails()
    }

    fun toggleCrewUpcomingHidden() {
        baseViewState.update {
            it.copy(
                crewUpcomingHidden = !it.crewUpcomingHidden
            )
        }
        updateDisplayedDetails()
    }

    fun toggleCrewPreviousHidden() {
        baseViewState.update {
            it.copy(
                crewPreviousHidden = !it.crewPreviousHidden
            )
        }
        updateDisplayedDetails()
    }

    private fun updateDisplayedDetails() {
        val viewState = baseViewState.value
        details?.let { details ->
            getDisplayedPersonDetailsUseCase.execute(
                GetDisplayedPersonDetailsUseCaseParams(
                    details = details,
                    castUpcomingHidden = viewState.castUpcomingHidden,
                    castPreviousHidden = viewState.castPreviousHidden,
                    crewUpcomingHidden = viewState.crewUpcomingHidden,
                    crewPreviousHidden = viewState.crewPreviousHidden,
                )
            ).onValue { formattedDetails ->
                baseViewState.update {
                    it.copy(
                        details = formattedDetails
                    )
                }
            }
        }
    }

    data class ViewState(
        val details: DisplayedPersonDetails? = null,
        val castUpcomingHidden: Boolean = true,
        val castPreviousHidden: Boolean = false,
        val crewUpcomingHidden: Boolean = true,
        val crewPreviousHidden: Boolean = false,
    ) : State

    @AssistedFactory
    interface PersonDetailsViewModelFactory {
        fun create(
            personId: Int,
        ): PersonDetailsViewModel
    }

}