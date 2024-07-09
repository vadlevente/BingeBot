package com.vadlevente.bingebot.list

import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.DisplayedMovie
import com.vadlevente.bingebot.core.model.Genre
import com.vadlevente.bingebot.core.model.NavDestination.SEARCH_MOVIE
import com.vadlevente.bingebot.core.ui.BaseViewModel
import com.vadlevente.bingebot.core.ui.State
import com.vadlevente.bingebot.list.MovieListViewModel.ViewState
import com.vadlevente.bingebot.list.domain.usecase.GetGenresUseCase
import com.vadlevente.bingebot.list.domain.usecase.GetMoviesUseCase
import com.vadlevente.bingebot.list.domain.usecase.GetMoviesUseCaseParams
import com.vadlevente.bingebot.list.domain.usecase.UpdateMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    private val updateMoviesUseCase: UpdateMoviesUseCase,
    private val getMoviesUseCase: GetMoviesUseCase,
    private val getGenresUseCase: GetGenresUseCase,
) : BaseViewModel<ViewState>(
    navigationEventChannel, toastEventChannel
) {

    private val viewState = MutableStateFlow(ViewState())
    override val state: StateFlow<ViewState> = viewState

    init {
        updateMoviesUseCase.execute(Unit).onStart()
        getMovies()
        getGenresUseCase.execute(Unit)
            .onValue { genres ->
                viewState.update {
                    it.copy(
                        genres = genres,
                        selectedGenres = emptyList(),
                    )
                }
            }
    }

    fun onNavigateToSearch() {
        navigateTo(SEARCH_MOVIE)
    }

    fun onNavigateToDetails(movieId: Int) {

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
        val genres: List<Genre> = emptyList(),
        val selectedGenres: List<Genre> = emptyList(),
        val isSearchFieldVisible: Boolean = false,
        val searchQuery: String? = null,
    ) : State

}