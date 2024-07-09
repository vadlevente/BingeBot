package com.vadlevente.bingebot.search

import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastType.INFO
import com.vadlevente.bingebot.core.model.DisplayedMovie
import com.vadlevente.bingebot.core.model.Movie
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.util.Constants.QUERY_MINIMUM_LENGTH
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.State
import com.vadlevente.bingebot.search.SearchMovieViewModel.ViewState
import com.vadlevente.bingebot.search.usecase.GetSearchResultUseCase
import com.vadlevente.bingebot.search.usecase.SaveMovieUseCase
import com.vadlevente.bingebot.search.usecase.SaveMovieUseCaseParams
import com.vadlevente.bingebot.search.usecase.SearchMovieUseCase
import com.vadlevente.bingebot.search.usecase.SearchMovieUseCaseParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchMovieViewModel @Inject constructor(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    private val getSearchResultUseCase: GetSearchResultUseCase,
    private val searchMovieUseCase: SearchMovieUseCase,
    private val saveMovieUseCase: SaveMovieUseCase,
) : BaseViewModel<ViewState>(
    navigationEventChannel, toastEventChannel
) {

    private val viewState = MutableStateFlow(ViewState())
    override val state: StateFlow<ViewState> = viewState

    init {
        getSearchResultUseCase.execute(Unit)
            .onValue { movies ->
                viewState.update {
                    it.copy(
                        movies = movies,
                    )
                }
            }
    }

    fun onQueryChanged(query: String) {
        viewState.update {
            it.copy(
                query = query,
            )
        }
        if (query.length < QUERY_MINIMUM_LENGTH) return
        searchMovieUseCase.execute(
            SearchMovieUseCaseParams(query)
        ).onStart()
    }

    fun onSaveMovie(movie: Movie) {
        saveMovieUseCase.execute(SaveMovieUseCaseParams(movie))
            .onValue {
                showToast(
                    stringOf(R.string.searchMovies_saveSuccessful),
                    INFO,
                )
                navigateUp()
            }
    }

    fun onBackPressed() {
        navigateUp()
    }

    data class ViewState(
        val query: String = "",
        val movies: List<DisplayedMovie> = emptyList(),
    ) : State

}