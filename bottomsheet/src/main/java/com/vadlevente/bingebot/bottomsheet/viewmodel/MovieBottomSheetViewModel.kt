package com.vadlevente.bingebot.bottomsheet.viewmodel

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.bottomsheet.R
import com.vadlevente.bingebot.bottomsheet.domain.usecases.DeleteMovieUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.DeleteMovieUseCaseParams
import com.vadlevente.bingebot.bottomsheet.domain.usecases.RemoveMovieFromWatchListUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.RemoveMovieFromWatchListUseCaseParams
import com.vadlevente.bingebot.bottomsheet.domain.usecases.SaveMovieUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.SaveMovieUseCaseParams
import com.vadlevente.bingebot.bottomsheet.domain.usecases.SetMovieSeenUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.SetMovieSeenUseCaseParams
import com.vadlevente.bingebot.bottomsheet.viewmodel.MovieBottomSheetViewModel.ViewState
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
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.State
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
import com.vadlevente.bingebot.resources.R as Res

@HiltViewModel
class MovieBottomSheetViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    private val bottomSheetEventChannel: BottomSheetEventChannel,
    private val dialogEventChannel: DialogEventChannel,
    private val deleteMovieUseCase: DeleteMovieUseCase,
    private val saveMovieUseCase: SaveMovieUseCase,
    private val setMovieSeenUseCase: SetMovieSeenUseCase,
    private val removeMovieFromWatchListUseCase: RemoveMovieFromWatchListUseCase,
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

    fun removeFromWatchList() {
        viewState.value.event?.watchListId?.let { watchListId ->
            viewState.value.event?.movie?.let { movie ->
                viewModelScope.launch {
                    dialogEventChannel.sendEvent(
                        ShowDialog(
                            title = stringOf(R.string.movieBottomSheet_removeMovieFromWatchListConfirmationTitle),
                            content = stringOf(R.string.movieBottomSheet_removeMovieFromWatchListConfirmationDescription),
                            positiveButtonTitle = stringOf(Res.string.common_Yes),
                            negativeButtonTitle = stringOf(Res.string.common_No),
                            onPositiveButtonClicked = {
                                removeMovieFromWatchListUseCase.execute(
                                    RemoveMovieFromWatchListUseCaseParams(
                                        movieId = movie.movie.id,
                                        watchListId = watchListId,
                                    )
                                )
                                    .onValue {
                                        onDismiss()
                                        showToast(
                                            message = stringOf(Res.string.successfulDeleteToast),
                                            type = INFO,
                                        )
                                    }
                            },
                        )
                    )
                }
            }
        }
    }

    fun onDelete() {
        val movieId = viewState.value.event?.movie?.movie?.id ?: return
        viewModelScope.launch {
            dialogEventChannel.sendEvent(
                ShowDialog(
                    title = stringOf(R.string.movieBottomSheet_deleteConfirmationTitle),
                    content = stringOf(R.string.movieBottomSheet_deleteConfirmationDescription),
                    positiveButtonTitle = stringOf(Res.string.common_Yes),
                    negativeButtonTitle = stringOf(Res.string.common_No),
                    onPositiveButtonClicked = {
                        deleteMovieUseCase.execute(
                            DeleteMovieUseCaseParams(movieId)
                        )
                            .onValue {
                                onDismiss()
                                showToast(
                                    message = stringOf(Res.string.successfulDeleteToast),
                                    type = INFO,
                                )
                            }
                    },
                )
            )
        }
    }

    fun onSetMovieWatched(dateMillis: Long) {
        viewState.value.event?.let {
            setMovieSeenUseCase.execute(
                SetMovieSeenUseCaseParams(
                    movieId = it.movie.movie.id,
                    watchedDate = Date(dateMillis),
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