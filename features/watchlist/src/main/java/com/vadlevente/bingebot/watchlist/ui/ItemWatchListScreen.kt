package com.vadlevente.bingebot.watchlist.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.ui.composables.BBOutlinedTextField
import com.vadlevente.bingebot.core.ui.composables.ListItem
import com.vadlevente.bingebot.core.ui.composables.ProgressScreen
import com.vadlevente.bingebot.core.ui.composables.TopBar
import com.vadlevente.bingebot.core.util.asOneDecimalString
import com.vadlevente.bingebot.core.util.yearString
import com.vadlevente.bingebot.ui.BingeBotTheme
import com.vadlevente.bingebot.watchlist.viewmodel.ItemWatchListViewModel
import com.vadlevente.bingebot.watchlist.viewmodel.ItemWatchListViewModel.ViewState
import com.vadlevente.bingebot.resources.R as Res

@Composable
fun <T : Item> ItemWatchListScreen(
    watchListId: String,
    viewModel: ItemWatchListViewModel<T>,
    resources: ItemWatchListResources,
) {
    val state by viewModel.state.collectAsState()
    val isInProgress by viewModel.isInProgress.collectAsState()
    LaunchedEffect(true) {
        viewModel.onInit(watchListId)
    }
    ItemWatchListScreenComponent(
        state = state,
        resources = resources,
        isInProgress = isInProgress,
        onToggleSearchField = viewModel::onToggleSearchField,
        onQueryChanged = viewModel::onQueryChanged,
        onNavigateToOptions = viewModel::onNavigateToOptions,
        onDeleteWatchList = viewModel::onDeleteWatchList,
        onBackPressed = viewModel::onBackPressed,
        onNavigateToSearch = viewModel::onNavigateToSearch,
    )
}

@Composable
fun <T : Item> ItemWatchListScreenComponent(
    state: ViewState<T>,
    resources: ItemWatchListResources,
    isInProgress: Boolean,
    onToggleSearchField: () -> Unit,
    onQueryChanged: (String) -> Unit,
    onNavigateToOptions: (Int) -> Unit,
    onDeleteWatchList: () -> Unit,
    onBackPressed: () -> Unit,
    onNavigateToSearch: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringOf(state.title ?: ""),
                canNavigateBack = true,
                onBackPressed = onBackPressed,
                actions = {
                    Icon(
                        imageVector = Filled.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable { onToggleSearchField() }
                            .padding(end = 8.dp)
                    )
                    Icon(
                        imageVector = Filled.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable { onDeleteWatchList() }
                            .padding(end = 8.dp)
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToSearch,
                containerColor = BingeBotTheme.colors.highlight,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(Filled.Add, "")
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
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    value = state.searchQuery ?: "",
                    hint = stringOf(resources.searchFieldHint),
                    onValueChange = onQueryChanged,
                )
            }
            ProgressScreen(
                isProgressVisible = isInProgress,
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.items.isEmpty()) {
                    val descriptionStringRes =
                        if (state.searchQuery == null) resources.emptyList
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
                        items(state.items) { displayedItem ->
                            val item = displayedItem.item
                            ListItem(
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

data class ItemWatchListResources(
    val searchFieldHint: Int,
    val emptyList: Int,
)