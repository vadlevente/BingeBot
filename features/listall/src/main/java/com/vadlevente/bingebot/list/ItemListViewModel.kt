package com.vadlevente.bingebot.list

import androidx.lifecycle.viewModelScope
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.events.toast.ToastEventChannel
import com.vadlevente.bingebot.core.model.DisplayedItem
import com.vadlevente.bingebot.core.model.Genre
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.model.SkeletonFactory
import com.vadlevente.bingebot.core.viewModel.BaseViewModel
import com.vadlevente.bingebot.core.viewModel.State
import com.vadlevente.bingebot.list.ItemListViewModel.ViewState
import com.vadlevente.bingebot.list.domain.model.DisplayedGenre
import com.vadlevente.bingebot.list.domain.usecase.ItemListUseCases
import com.vadlevente.bingebot.list.domain.usecase.SetIsWatchedFilterUseCaseParams
import com.vadlevente.bingebot.list.domain.usecase.SetQueryFilterUseCaseParams
import com.vadlevente.bingebot.list.domain.usecase.SetSelectedGenresUseCaseParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class ItemListViewModel<T : Item>(
    navigationEventChannel: NavigationEventChannel,
    toastEventChannel: ToastEventChannel,
    private val useCases: ItemListUseCases<T>,
) : BaseViewModel<ViewState<T>>(
    navigationEventChannel, toastEventChannel
) {

    protected val baseViewState = MutableStateFlow(ViewState<T>())
    override val state: StateFlow<ViewState<T>> = baseViewState
    abstract val skeletonFactory: SkeletonFactory<T>

    private var isInitialized = false

    init {
        combine(
            useCases.updateItemsUseCase.execute(Unit),
            useCases.updateWatchListsUseCase.execute(Unit),
            useCases.getFiltersUseCase.execute(Unit),
            ::Triple,
        ).onEach { (_, _, filters) ->
            baseViewState.update {
                it.copy(
                    genres = filters.displayedGenres,
                    isAnyGenreSelected = filters.displayedGenres.any { it.isSelected },
                    isWatchedSelected = filters.isWatchedSelected,
                    searchQuery = filters.searchQuery,
                )
            }
            if (!isInitialized) {
                baseViewState.update {
                    it.copy(
                        isSearchFieldVisible = !filters.searchQuery.isNullOrEmpty(),
                        areFiltersVisible = filters.displayedGenres.any { it.isSelected } || filters.isWatchedSelected != null
                    )
                }
                isInitialized = true
            }
        }.flatMapMerge {
            useCases.getItemsUseCase.execute(Unit)
        }.onValue { items ->
            viewModelScope.launch {
                baseViewState.update {
                    it.copy(
                        items = items
                    )
                }
            }
        }

        buildList {
            repeat(10) {
                add(DisplayedItem(skeletonFactory.createSkeleton(), null))
            }
        }.let { skeletons ->
            baseViewState.update {
                it.copy(
                    items = skeletons
                )
            }
        }
    }

    abstract fun onNavigateToSearch()
    abstract fun onNavigateToOptions(itemId: Int)
    abstract fun onOpenWatchLists()
    abstract fun onOpenOrderBy()

    fun onToggleSearchField() {
        onQueryChanged(if (baseViewState.value.isSearchFieldVisible) null else "")
        baseViewState.update {
            it.copy(
                isSearchFieldVisible = !it.isSearchFieldVisible,
            )
        }
    }

    fun onToggleFilters() {
        baseViewState.update {
            it.copy(
                areFiltersVisible = !it.areFiltersVisible
            )
        }
    }

    fun onQueryChanged(value: String?) {
        useCases.setQueryFilterUseCase.execute(
            SetQueryFilterUseCaseParams(value)
        ).onStart()
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

    fun onDestroyScreen() {
        onClearIsWatched()
        onClearGenres()
        onQueryChanged(null)
    }

    data class ViewState<T : Item>(
        val items: List<DisplayedItem<T>> = emptyList(),
        val genres: List<DisplayedGenre> = emptyList(),
        val isSearchFieldVisible: Boolean = false,
        val searchQuery: String? = null,
        val isAnyGenreSelected: Boolean = false,
        val isWatchedSelected: Boolean? = null,
        val areFiltersVisible: Boolean = false,
    ) : State

}