package com.vadlevente.bingebot.search.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.vadlevente.bingebot.core.ui.composables.TopBar
import com.vadlevente.bingebot.core.util.Constants.QUERY_MINIMUM_LENGTH
import com.vadlevente.bingebot.core.util.asOneDecimalString
import com.vadlevente.bingebot.core.util.yearString
import com.vadlevente.bingebot.search.R
import com.vadlevente.bingebot.search.SearchItemViewModel
import com.vadlevente.bingebot.search.SearchItemViewModel.ViewState

@Composable
fun <T : Item> SearchItemScreen(
    viewModel: SearchItemViewModel<T>,
    resources: SearchItemScreenResources,
) {
    val state by viewModel.state.collectAsState()
    SearchItemScreenComponent(
        state = state,
        resources = resources,
        onQueryChanged = viewModel::onQueryChanged,
        onNavigateToOptions = viewModel::onNavigateToOptions,
        onBackPressed = viewModel::onBackPressed,
    )
}

@Composable
fun <T : Item> SearchItemScreenComponent(
    state: ViewState<T>,
    resources: SearchItemScreenResources,
    onQueryChanged: (String) -> Unit,
    onNavigateToOptions: (Int) -> Unit,
    onBackPressed: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringOf(resources.title),
                canNavigateBack = true,
                onBackPressed = onBackPressed,
            )
        },
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
        ) {
            BBOutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                value = state.query,
                hint = stringOf(resources.searchFieldHint),
                onValueChange = onQueryChanged,
            )
            Box(modifier = Modifier.fillMaxSize()) {
                if (state.query.length < QUERY_MINIMUM_LENGTH) {
                    Text(
                        text = stringResource(resources.queryDescription),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            textAlign = TextAlign.Center
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.Center),
                    )
                } else if (state.items.isEmpty()) {
                    Text(
                        text = stringResource(R.string.searchItems_emptyListDescription),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            textAlign = TextAlign.Center
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.Center),
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        items(
                            items = state.items,
                            key = { it.item.id }
                        ) { displayedItem ->
                            val item = displayedItem.item
                            ListItem(
                                isLoading = false,
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

data class SearchItemScreenResources(
    val title: Int,
    val searchFieldHint: Int,
    val queryDescription: Int,
)