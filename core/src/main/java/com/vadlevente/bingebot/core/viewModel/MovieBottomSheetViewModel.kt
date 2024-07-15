package com.vadlevente.bingebot.core.viewModel

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.core.R
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowAddMovieToWatchListBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowMovieBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEventChannel
import com.vadlevente.bingebot.core.events.dialog.DialogEvent.ShowDialog
import com.vadlevente.bingebot.core.events.dialog.DialogEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastType.INFO
import com.vadlevente.bingebot.core.model.Movie
import com.vadlevente.bingebot.core.model.NavDestination.MOVIE_DETAILS
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.usecase.DeleteMovieUseCase
import com.vadlevente.bingebot.core.usecase.DeleteMovieUseCaseParams
import com.vadlevente.bingebot.core.usecase.SaveMovieUseCase
import com.vadlevente.bingebot.core.usecase.SaveMovieUseCaseParams
import com.vadlevente.bingebot.core.usecase.SetMovieSeenUseCase
import com.vadlevente.bingebot.core.usecase.SetMovieSeenUseCaseParams
import com.vadlevente.bingebot.core.viewModel.MovieBottomSheetViewModel.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MovieBottomSheetViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    private val bottomSheetEventChannel: BottomSheetEventChannel,
    private val dialogEventChannel: DialogEventChannel,
    private val deleteMovieUseCase: DeleteMovieUseCase,
    private val saveMovieUseCase: SaveMovieUseCase,
    private val setMovieSeenUseCase: SetMovieSeenUseCase,
) : BaseViewModel<ViewState>(
    navigationEventChannel, toastEventChannel
) {

    private val viewState = MutableStateFlow(ViewState())
    override val state: StateFlow<ViewState> = viewState

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

    fun onDismiss() {
        viewState.update {
            it.copy(
                isVisible = false,
            )
        }
    }

    fun onSaveMovie(movie: Movie) {
        saveMovieUseCase.execute(SaveMovieUseCaseParams(movie))
            .onValue {
                showToast(
                    stringOf(R.string.movieBottomSheet_saveSuccessful),
                    INFO,
                )
                navigateUp()
                onDismiss()
            }
    }

    fun onAddToWatchList() {
        viewState.value.event?.let { event ->
            val displayedMovie = event.movie
            viewModelScope.launch {
                bottomSheetEventChannel.sendEvent(
                    ShowAddMovieToWatchListBottomSheet(
                        movie = displayedMovie,
                        alreadySaved = event.alreadySaved,
                    )
                )
            }
            onDismiss()
        }
    }

    fun onDelete() {
        val movieId = viewState.value.event?.movie?.movie?.id ?: return
        viewModelScope.launch {
            dialogEventChannel.sendEvent(
                ShowDialog(
                    title = stringOf(R.string.movieBottomSheet_deleteConfirmationTitle),
                    content = stringOf(R.string.movieBottomSheet_deleteConfirmationDescription),
                    positiveButtonTitle = stringOf(R.string.common_Yes),
                    negativeButtonTitle = stringOf(R.string.common_No),
                    onPositiveButtonClicked = {
                        deleteMovieUseCase.execute(
                            DeleteMovieUseCaseParams(movieId)
                        )
                            .onValue {
                                onDismiss()
                                showToast(
                                    message = stringOf(R.string.movieBottomSheet_movieDeletedToast),
                                    type = INFO,
                                )
                            }
                    },
                )
            )
        }
    }

    fun onSetMovieWatched() {
        viewState.value.event?.let {
            setMovieSeenUseCase.execute(
                SetMovieSeenUseCaseParams(
                    movieId = it.movie.movie.id,
                    watchedDate = Date(),
                )
            ).onValue {
                onDismiss()
                showToast(
                    message = stringOf(R.string.movieBottomSheet_setWatchedDateSuccessful),
                    type = INFO,
                )
            }
        }
    }

    fun onSetMovieNotWatched() {
        viewState.value.event?.let {
            setMovieSeenUseCase.execute(
                SetMovieSeenUseCaseParams(
                    movieId = it.movie.movie.id,
                    watchedDate = null,
                )
            ).onValue {
                onDismiss()
                showToast(
                    message = stringOf(R.string.movieBottomSheet_revertWatchedDateSuccessful),
                    type = INFO,
                )
            }
        }
    }

    fun onShowDetails() {
        viewState.value.event?.let { event ->
            navigateTo(MOVIE_DETAILS, event.movie.movie.id)
            onDismiss()
        }
    }

    data class ViewState(
        val isVisible: Boolean = false,
        val event: ShowMovieBottomSheet? = null,
    ) : State

}