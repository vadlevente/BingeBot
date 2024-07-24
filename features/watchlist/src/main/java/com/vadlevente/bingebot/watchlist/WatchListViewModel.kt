package com.vadlevente.bingebot.watchlist

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowItemBottomSheet.ShowMovieBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEventChannel
import com.vadlevente.bingebot.core.events.dialog.DialogEvent.ShowDialog
import com.vadlevente.bingebot.core.events.dialog.DialogEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastType.INFO
import com.vadlevente.bingebot.core.model.DisplayedItem
import com.vadlevente.bingebot.core.model.Movie
import com.vadlevente.bingebot.core.model.NavDestination.SEARCH_MOVIE
import com.vadlevente.bingebot.core.model.exception.Reason.DATA_NOT_FOUND
import com.vadlevente.bingebot.core.model.exception.isBecauseOf
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.State
import com.vadlevente.bingebot.watchlist.WatchListViewModel.ViewState
import com.vadlevente.bingebot.watchlist.domain.usecase.DeleteWatchListUseCase
import com.vadlevente.bingebot.watchlist.domain.usecase.DeleteWatchListUseCaseParams
import com.vadlevente.bingebot.watchlist.domain.usecase.GetWatchListMoviesUseCase
import com.vadlevente.bingebot.watchlist.domain.usecase.GetWatchListMoviesUseCaseParams
import com.vadlevente.bingebot.watchlist.domain.usecase.GetWatchListUseCase
import com.vadlevente.bingebot.watchlist.domain.usecase.GetWatchListUseCaseParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.vadlevente.bingebot.resources.R as Res

@HiltViewModel
class WatchListViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    private val getWatchListMoviesUseCase: GetWatchListMoviesUseCase,
    private val bottomSheetEventChannel: BottomSheetEventChannel,
    private val getWatchListUseCase: GetWatchListUseCase,
    private val deleteWatchListUseCase: DeleteWatchListUseCase,
    private val dialogEventChannel: DialogEventChannel,
) : BaseViewModel<ViewState>(
    navigationEventChannel, toastEventChannel
) {

    private val viewState = MutableStateFlow(ViewState())
    override val state: StateFlow<ViewState> = viewState
    override val basicErrorHandler: (Throwable) -> Unit = {
        if (it.isBecauseOf(DATA_NOT_FOUND)) {
            navigateUp()
        } else {
            super.basicErrorHandler(it)
        }
    }

    private lateinit var watchListId: String

    fun onInit(watchListId: String) {
        this.watchListId = watchListId
        getWatchListUseCase.execute(
            GetWatchListUseCaseParams(
                watchListId = watchListId
            )
        ).onValue { watchList ->
            viewState.update {
                it.copy(
                    title = watchList.title,
                )
            }
            getMovies()
        }
    }

    fun onNavigateToSearch() {
        navigateTo(SEARCH_MOVIE)
    }

    fun onDeleteWatchList() {
        viewModelScope.launch {
            dialogEventChannel.sendEvent(
                ShowDialog(
                    title = stringOf(R.string.deleteWatchList_confirm_title),
                    content = stringOf(R.string.deleteWatchList_confirm_description),
                    positiveButtonTitle = stringOf(Res.string.common_Yes),
                    negativeButtonTitle = stringOf(Res.string.common_No),
                    onPositiveButtonClicked = {
                        deleteWatchListUseCase.execute(
                            DeleteWatchListUseCaseParams(
                                watchListId = watchListId
                            )
                        )
                            .onValue {
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

    fun onToggleSearchField() {
        viewState.update {
            it.copy(
                isSearchFieldVisible = !it.isSearchFieldVisible,
                searchQuery = if (it.isSearchFieldVisible) null else "",
            )
        }
        getMovies()
    }

    fun onQueryChanged(value: String) {
        viewState.update {
            it.copy(
                searchQuery = value,
            )
        }
        getMovies()
    }

    fun onNavigateToOptions(movieId: Int) {
        viewState.value.movies.firstOrNull { it.item.id == movieId }?.let {
            viewModelScope.launch {
                bottomSheetEventChannel.sendEvent(
                    ShowMovieBottomSheet(
                        item = it,
                        alreadySaved = true,
                        watchListId = watchListId,
                    )
                )
            }
        }
    }

    fun onBackPressed() {
        navigateUp()
    }

    private fun getMovies() {
        getWatchListMoviesUseCase.execute(
            GetWatchListMoviesUseCaseParams(
                watchListId = watchListId,
                query = viewState.value.searchQuery,
            )
        ).onValue { movies ->
            viewState.update {
                it.copy(
                    movies = movies
                )
            }
        }
    }

    data class ViewState(
        val title: String? = null,
        val movies: List<DisplayedItem<Movie>> = emptyList(),
        val isSearchFieldVisible: Boolean = false,
        val searchQuery: String? = null,
    ) : State

}
