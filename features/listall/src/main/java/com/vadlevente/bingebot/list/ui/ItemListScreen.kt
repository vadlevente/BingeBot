package com.vadlevente.bingebot.list.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vadlevente.bingebot.core.model.Genre
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.ui.composables.BBFilterChip
import com.vadlevente.bingebot.core.ui.composables.BBOutlinedTextField
import com.vadlevente.bingebot.core.ui.composables.LifecycleEvents
import com.vadlevente.bingebot.core.ui.composables.ListItem
import com.vadlevente.bingebot.core.ui.composables.ProgressScreen
import com.vadlevente.bingebot.core.ui.composables.TopBar
import com.vadlevente.bingebot.core.util.asOneDecimalString
import com.vadlevente.bingebot.core.util.yearString
import com.vadlevente.bingebot.list.ItemListViewModel
import com.vadlevente.bingebot.list.ItemListViewModel.ViewState
import com.vadlevente.bingebot.list.R
import com.vadlevente.bingebot.ui.BingeBotTheme
import com.vadlevente.bingebot.resources.R as Res

@Composable
fun <T : Item> ItemListScreen(
    viewModel: ItemListViewModel<T>,
    resources: ItemListScreenResources,
) {
    LifecycleEvents(
        onDestroy = viewModel::onDestroyScreen
    )
    val state by viewModel.state.collectAsState()
    val isInProgress by viewModel.isInProgress.collectAsState()
    ItemListScreenComponent(
        state = state,
        resources = resources,
        isInProgress = isInProgress,
        onNavigateToSearch = viewModel::onNavigateToSearch,
        onToggleSearchField = viewModel::onToggleSearchField,
        onToggleFilters = viewModel::onToggleFilters,
        onQueryChanged = viewModel::onQueryChanged,
        onNavigateToOptions = viewModel::onNavigateToOptions,
        onClearGenres = viewModel::onClearGenres,
        onToggleGenre = viewModel::onToggleGenre,
        onOpenWatchLists = viewModel::onOpenWatchLists,
        onToggleIsWatched = viewModel::onToggleIsWatched,
        onClearIsWatched = viewModel::onClearIsWatched,
        onOpenOrderBy = viewModel::onOpenOrderBy
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Item> ItemListScreenComponent(
    state: ViewState<T>,
    resources: ItemListScreenResources,
    isInProgress: Boolean,
    onNavigateToSearch: () -> Unit,
    onToggleSearchField: () -> Unit,
    onToggleFilters: () -> Unit,
    onQueryChanged: (String) -> Unit,
    onNavigateToOptions: (Int) -> Unit,
    onClearGenres: () -> Unit,
    onToggleGenre: (Genre) -> Unit,
    onOpenWatchLists: () -> Unit,
    onToggleIsWatched: (Boolean) -> Unit,
    onClearIsWatched: () -> Unit,
    onOpenOrderBy: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringOf(resources.title),
                actions = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Sort,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable { onOpenOrderBy() }
                            .padding(end = 8.dp)
                    )
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null,
                        tint = if (state.isSearchFieldVisible) BingeBotTheme.colors.highlight else MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable { onToggleSearchField() }
                            .padding(end = 8.dp)
                    )
                    BadgedBox(
                        modifier = Modifier.padding(end = 8.dp),
                        badge = {
                            if (state.isWatchedSelected != null || state.isAnyGenreSelected) {
                                Badge(containerColor = BingeBotTheme.colors.highlight)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FilterAlt,
                            contentDescription = null,
                            tint = if (state.areFiltersVisible) BingeBotTheme.colors.highlight else MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clickable { onToggleFilters() }
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable { onOpenWatchLists() }
                            .padding(end = 8.dp)
                    )
                }
            )
        },
        bottomBar = {},
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToSearch,
                containerColor = BingeBotTheme.colors.highlight,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(Icons.Filled.Add, "")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            AnimatedVisibility(visible = state.isSearchFieldVisible) {
                BBOutlinedTextField(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    value = state.searchQuery ?: "",
                    hint = stringOf(resources.searchFieldHint),
                    onValueChange = onQueryChanged,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                    )
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .padding(top = 8.dp)
                    .background(MaterialTheme.colorScheme.onBackground)
            )
            AnimatedVisibility(visible = state.areFiltersVisible) {
                Column {
                    GenreSelector(
                        state = state,
                        onToggleGenre = onToggleGenre,
                        onClearGenres = onClearGenres,
                    )
                    IsWatchedSelector(
                        state = state,
                        onToggleIsWatched = onToggleIsWatched,
                        onClearIsWatched = onClearIsWatched,
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.onBackground)
                            .padding(bottom = 4.dp)
                    )
                }
            }
            ProgressScreen(
                isProgressVisible = isInProgress,
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.items.isEmpty()) {
                    val descriptionStringRes =
                        if (state.searchQuery == null) resources.emptyListDescription
                        else Res.string.emptyQueriedListDescription
                    Text(
                        text = stringResource(descriptionStringRes),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            textAlign = TextAlign.Center
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.Center),
                    )
                } else {
                    LazyColumn {
                        items(state.items) { displayedMovie ->
                            val movie = displayedMovie.item
                            ListItem(
                                title = movie.title,
                                iconPath = displayedMovie.thumbnailUrl,
                                watchedDate = movie.watchedDate,
                                rating = movie.voteAverage.asOneDecimalString,
                                releaseYear = movie.releaseDate?.yearString ?: "",
                                onClick = { onNavigateToOptions(movie.id) },
                            )
                        }
                    }
                }
            }

        }
    }

}

@Composable
private fun GenreSelector(
    state: ViewState<*>,
    onToggleGenre: (Genre) -> Unit,
    onClearGenres: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LazyRow(modifier = Modifier.weight(1f)) {
            items(state.genres) { genre ->
                BBFilterChip(
                    title = genre.genre.name,
                    isSelected = genre.isSelected,
                    onClicked = { onToggleGenre(genre.genre) },
                )
            }
        }
        if (state.isAnyGenreSelected) {
            Icon(
                imageVector = Icons.Filled.Clear,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable { onClearGenres() }
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun IsWatchedSelector(
    state: ViewState<*>,
    onToggleIsWatched: (Boolean) -> Unit,
    onClearIsWatched: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BBFilterChip(
            title = stringResource(id = R.string.isWatchedFilter_trueLabel),
            isSelected = state.isWatchedSelected == true,
            onClicked = { onToggleIsWatched(true) },
        )
        BBFilterChip(
            title = stringResource(id = R.string.isWatchedFilter_falseLabel),
            isSelected = state.isWatchedSelected == false,
            onClicked = { onToggleIsWatched(false) },
        )
        if (state.isWatchedSelected != null) {
            Icon(
                imageVector = Icons.Filled.Clear,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable { onClearIsWatched() }
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

data class ItemListScreenResources(
    val title: Int,
    val searchFieldHint: Int,
    val emptyListDescription: Int,
)