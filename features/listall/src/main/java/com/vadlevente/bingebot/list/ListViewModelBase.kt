package com.vadlevente.bingebot.list

import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.DisplayedItem
import com.vadlevente.bingebot.core.model.Genre
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.State
import com.vadlevente.bingebot.list.ListViewModelBase.ViewState
import com.vadlevente.bingebot.list.domain.model.DisplayedGenre
import com.vadlevente.bingebot.list.domain.usecase.GetItemsUseCaseParams
import com.vadlevente.bingebot.list.domain.usecase.ItemListUseCases
import com.vadlevente.bingebot.list.domain.usecase.SetIsWatchedFilterUseCaseParams
import com.vadlevente.bingebot.list.domain.usecase.SetSelectedGenresUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

abstract class ListViewModelBase <T: Item> (
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    private val useCases: ItemListUseCases<T>,
) : BaseViewModel<ViewState<T>>(
    navigationEventChannel, toastEventChannel
) {

    protected val baseViewState = MutableStateFlow(ViewState<T>())
    override val state: StateFlow<ViewState<T>> = baseViewState

    init {
        useCases.updateItemsUseCase.execute(Unit).onStart()
        useCases.updateWatchListsUseCase.execute(Unit).onStart()
        getItems()
        useCases.getFiltersUseCase.execute(Unit)
            .onValue { filters ->
                baseViewState.update {
                    it.copy(
                        genres = filters.displayedGenres,
                        isAnyGenreSelected = filters.displayedGenres.any { it.isSelected },
                        isWatchedSelected = filters.isWatchedSelected,
                    )
                }
            }
    }

    abstract fun onNavigateToSearch()
    abstract fun onNavigateToOptions(itemId: Int)
    abstract fun onOpenWatchLists()

    fun onToggleSearchField() {
        baseViewState.update {
            it.copy(
                isSearchFieldVisible = !it.isSearchFieldVisible,
                searchQuery = if (it.isSearchFieldVisible) null else "",
            )
        }
        getItems()
    }

    fun onToggleFilters() {
        baseViewState.update {
            it.copy(
                areFiltersVisible = !it.areFiltersVisible
            )
        }
    }

    fun onQueryChanged(value: String) {
        baseViewState.update {
            it.copy(
                searchQuery = value,
            )
        }
        getItems()
    }

    fun onClearGenres() {
        useCases.setSelectedGenresUseCase.execute(
            SetSelectedGenresUseCaseParams(
                emptyList(),
            )
        ).onStart()
    }

    fun onToggleGenre(genre: Genre) {
        val selectedGenres = baseViewState.value.genres.filter { it.isSelected }.map { it.genre }
        val modifiedGenres = if (selectedGenres.contains(genre)) {
            selectedGenres.minus(genre)
        } else {
            selectedGenres.plus(genre)
        }
        useCases.setSelectedGenresUseCase.execute(
            SetSelectedGenresUseCaseParams(
                modifiedGenres,
            )
        ).onStart()
    }

    fun onToggleIsWatched(value: Boolean) {
        useCases.setIsWatchedFilterUseCase.execute(
            SetIsWatchedFilterUseCaseParams(value)
        ).onStart()
    }

    fun onClearIsWatched() {
        useCases.setIsWatchedFilterUseCase.execute(
            SetIsWatchedFilterUseCaseParams(null)
        ).onStart()
    }

    private fun getItems() {
        useCases.getItemsUseCase.execute(
            GetItemsUseCaseParams(
                query = baseViewState.value.searchQuery,
            )
        ).onValue { items ->
            baseViewState.update {
                it.copy(
                    items = items
                )
            }
        }
    }

    data class ViewState<T: Item>(
        val items: List<DisplayedItem<T>> = emptyList(),
        val genres: List<DisplayedGenre> = emptyList(),
        val isSearchFieldVisible: Boolean = false,
        val searchQuery: String? = null,
        val isAnyGenreSelected: Boolean = false,
        val isWatchedSelected: Boolean? = null,
        val areFiltersVisible: Boolean = false,
    ) : State

}