package com.vadlevente.bingebot.list

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowMovieBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEvent.ShowWatchListsBottomSheet
import com.vadlevente.bingebot.core.events.bottomSheet.BottomSheetEventChannel
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.DisplayedMovie
import com.vadlevente.bingebot.core.model.Genre
import com.vadlevente.bingebot.core.model.NavDestination.SEARCH_MOVIE
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.State
import com.vadlevente.bingebot.list.MovieListViewModel.ViewState
import com.vadlevente.bingebot.list.domain.model.DisplayedGenre
import com.vadlevente.bingebot.list.domain.usecase.GetGenresUseCase
import com.vadlevente.bingebot.list.domain.usecase.GetMoviesUseCase
import com.vadlevente.bingebot.list.domain.usecase.GetMoviesUseCaseParams
import com.vadlevente.bingebot.list.domain.usecase.SetSelectedGenresUseCase
import com.vadlevente.bingebot.list.domain.usecase.SetSelectedGenresUseCaseParams
import com.vadlevente.bingebot.list.domain.usecase.UpdateMoviesUseCase
import com.vadlevente.bingebot.list.domain.usecase.UpdateWatchListsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    updateMoviesUseCase: UpdateMoviesUseCase,
    updateWatchListsUseCase: UpdateWatchListsUseCase,
    getGenresUseCase: GetGenresUseCase,
    private val getMoviesUseCase: GetMoviesUseCase,
    private val bottomSheetEventChannel: BottomSheetEventChannel,
    private val setSelectedGenresUseCase: SetSelectedGenresUseCase,
) : BaseViewModel<ViewState>(
    navigationEventChannel, toastEventChannel
) {

    private val viewState = MutableStateFlow(ViewState())
    override val state: StateFlow<ViewState> = viewState

    init {
        updateMoviesUseCase.execute(Unit).onStart()
        updateWatchListsUseCase.execute(Unit).onStart()
        getMovies()
        getGenresUseCase.execute(Unit)
            .onValue { genres ->
                viewState.update {
                    it.copy(
                        genres = genres,
                        isAnyGenreSelected = genres.any { it.isSelected }
                    )
                }
            }
    }

    fun onNavigateToSearch() {
        navigateTo(SEARCH_MOVIE)
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
        viewState.value.movies.firstOrNull { it.movie.id == movieId }?.let {
            viewModelScope.launch {
                bottomSheetEventChannel.sendEvent(
                    ShowMovieBottomSheet(
                        movie = it,
                        alreadySaved = true,
                    )
                )
            }
        }
    }

    fun onClearGenres() {
        setSelectedGenresUseCase.execute(
            SetSelectedGenresUseCaseParams(
                emptyList()
            )
        ).onStart()
    }

    fun onToggleGenre(genre: Genre) {
        val selectedGenres = viewState.value.genres.filter { it.isSelected }.map { it.genre }
        val modifiedGenres = if (selectedGenres.contains(genre)) {
            selectedGenres.minus(genre)
        } else {
            selectedGenres.plus(genre)
        }
        println("select genres: modify ${modifiedGenres.joinToString { it.name }}")
        setSelectedGenresUseCase.execute(
            SetSelectedGenresUseCaseParams(
                modifiedGenres
            )
        ).onStart()
    }

    fun onOpenWatchLists() {
        viewModelScope.launch {
            bottomSheetEventChannel.sendEvent(
                ShowWatchListsBottomSheet
            )
        }
    }

    private fun getMovies() {
        getMoviesUseCase.execute(
            GetMoviesUseCaseParams(
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
        val movies: List<DisplayedMovie> = emptyList(),
        val genres: List<DisplayedGenre> = emptyList(),
        val isSearchFieldVisible: Boolean = false,
        val searchQuery: String? = null,
        val isAnyGenreSelected: Boolean = false,
    ) : State

}