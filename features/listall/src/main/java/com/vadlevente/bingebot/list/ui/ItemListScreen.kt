package com.vadlevente.bingebot.list.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material.icons.filled.ViewCompact
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vadlevente.bingebot.core.model.Genre
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.ui.composables.BBFilterChip
import com.vadlevente.bingebot.core.ui.composables.BBIcon
import com.vadlevente.bingebot.core.ui.composables.ListItem
import com.vadlevente.bingebot.core.ui.composables.ListItemSmall
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
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isInProgress by viewModel.isInProgress.collectAsStateWithLifecycle()
    ItemListScreenComponent(
        state = state,
        resources = resources,
        isInProgress = isInProgress,
        onNavigateToSearch = viewModel::onNavigateToSearch,
        onQueryChanged = viewModel::onQueryChanged,
        onNavigateToOptions = viewModel::onNavigateToOptions,
        onClearGenres = viewModel::onClearGenres,
        onToggleGenre = viewModel::onToggleGenre,
        onOpenWatchLists = viewModel::onOpenWatchLists,
        onToggleIsWatched = viewModel::onToggleIsWatched,
        onOpenOrderBy = viewModel::onOpenOrderBy,
        onToggleViewSelector = viewModel::onToggleViewSelector,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Item> ItemListScreenComponent(
    state: ViewState<T>,
    resources: ItemListScreenResources,
    isInProgress: Boolean,
    onNavigateToSearch: () -> Unit,
    onQueryChanged: (String?) -> Unit,
    onNavigateToOptions: (Int) -> Unit,
    onClearGenres: () -> Unit,
    onToggleGenre: (Genre) -> Unit,
    onOpenWatchLists: () -> Unit,
    onToggleIsWatched: () -> Unit,
    onToggleViewSelector: () -> Unit,
    onOpenOrderBy: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    Scaffold(
        topBar = {
            Crossfade(
                targetState = state.isSearchFieldVisible,
                label = "searchfield"
            ) { isExpanded ->
                BackHandler(enabled = isExpanded) {
                    onQueryChanged(null)
                }
                if (isExpanded) {
                    SearchBar(
                        hint = stringResource(resources.searchFieldHint),
                        focusRequester = focusRequester,
                        onQueryChanged = onQueryChanged,
                        state = state,
                    )
                } else {
                    TopBar(
                        title = stringOf(resources.title),
                        actions = {
                            BBIcon(
                                imageVector = Icons.Filled.Search,
                                onClick = {
                                    onQueryChanged("")
                                },
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            BBIcon(
                                imageVector = Icons.AutoMirrored.Filled.List,
                                onClick = onOpenWatchLists,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    )
                }
            }

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

        val controlsVisible = remember { mutableStateOf(true) }
        val scrollState = rememberLazyListState()
        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    if (available.y < 0) {
                        controlsVisible.value = false
                    } else if (available.y > 10) {
                        controlsVisible.value = true
                    }
                    return Offset.Zero
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .nestedScroll(nestedScrollConnection)
        ) {
            AnimatedVisibility(
                visible = controlsVisible.value && !isInProgress,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                ControlSection(
                    state = state,
                    onToggleGenre = onToggleGenre,
                    onClearGenres = onClearGenres,
                    onOpenOrderBy = onOpenOrderBy,
                    onToggleIsWatched = onToggleIsWatched,
                    onToggleViewSelector = onToggleViewSelector,
                )
            }
            Box(modifier = Modifier.fillMaxSize()) {
                if (state.items.isEmpty()) {
                    val descriptionStringRes =
                        if (state.searchQuery == null) resources.emptyListDescription
                        else Res.string.emptyQueriedListDescription
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(descriptionStringRes),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            textAlign = TextAlign.Center
                        ),
                        color = MaterialTheme.colorScheme.primary,
                    )
                } else {
                    LazyColumn(state = scrollState) {
                        items(state.items) { displayedItem ->
                            val item = displayedItem.item
                            if (state.isSmallView) {
                                ListItemSmall(
                                    isLoading = isInProgress,
                                    modifier = Modifier,
                                    title = item.title,
                                    watchedDate = item.watchedDate,
                                    onClick = { onNavigateToOptions(item.id) },
                                )
                            } else {
                                ListItem(
                                    isLoading = isInProgress,
                                    modifier = Modifier,
                                    title = item.title,
                                    iconPath = displayedItem.thumbnailUrl,
                                    watchedDate = item.watchedDate,
                                    rating = item.voteAverage.asOneDecimalString,
                                    releaseYear = item.releaseDate?.yearString ?: "",
                                    onClick = { onNavigateToOptions(item.id) },
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}

@Composable
private fun <T : Item> SearchBar(
    hint: String,
    focusRequester: FocusRequester,
    onQueryChanged: (String?) -> Unit,
    state: ViewState<T>,
) {
    LaunchedEffect(true) {
        focusRequester.requestFocus()
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clickable {
                    onQueryChanged(null)
                }
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = Icons.Filled.ChevronLeft,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary,
            )
        }
        val textFieldValue = TextFieldValue(
            state.searchQuery ?: "",
            TextRange(state.searchQuery?.length ?: 0)
        )
        TextField(
            modifier = Modifier
                .padding(end = 8.dp)
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = textFieldValue,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.primary,
            ),
            onValueChange = {
                onQueryChanged(it.text)
            },
            trailingIcon = {
                Icon(
                    modifier = Modifier
                        .padding(8.dp),
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            },
            placeholder = {
                Text(
                    text = hint,
                    color = MaterialTheme.colorScheme.primary,
                )
            },
        )
    }
}

@Composable
private fun ControlSection(
    state: ViewState<*>,
    onToggleGenre: (Genre) -> Unit,
    onClearGenres: () -> Unit,
    onOpenOrderBy: () -> Unit,
    onToggleIsWatched: () -> Unit,
    onToggleViewSelector: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        GenreSelector(
            state = state,
            onToggleGenre = onToggleGenre,
            onClearGenres = onClearGenres,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier.clickable {
                    onOpenOrderBy()
                },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier
                        .padding(8.dp),
                    imageVector = Icons.Default.SortByAlpha,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.sorting_title),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            BBFilterChip(
                title = when (state.isWatchedSelected) {
                    true -> R.string.isWatchedFilter_trueLabel
                    false -> R.string.isWatchedFilter_falseLabel
                    null -> R.string.isWatchedFilter_allLabel
                }.let { stringResource(it) },
                isSelected = state.isWatchedSelected != null,
                onClicked = onToggleIsWatched,
            )
            Icon(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        onToggleViewSelector()
                    },
                imageVector = Icons.Default.ViewCompact,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )
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

data class ItemListScreenResources(
    val title: Int,
    val searchFieldHint: Int,
    val emptyListDescription: Int,
)