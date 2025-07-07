package com.vadlevente.moviedetails.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.ui.composables.BBIcon
import com.vadlevente.bingebot.core.ui.composables.TopBar
import com.vadlevente.bingebot.core.ui.composables.shimmerEffect
import com.vadlevente.bingebot.core.util.asOneDecimalString
import com.vadlevente.bingebot.core.util.dateString
import com.vadlevente.bingebot.details.R
import com.vadlevente.bingebot.ui.BingeBotTheme
import com.vadlevente.moviedetails.ItemDetailsViewModel
import com.vadlevente.moviedetails.domain.model.DisplayedProviderInfo
import com.vadlevente.bingebot.resources.R as Res

@Composable
fun <T : Item> ItemDetailsScreen(
    viewModel: ItemDetailsViewModel<T>,
    customContent: @Composable LazyItemScope.() -> Unit,
    dateContent: @Composable RowScope.() -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isInProgress by viewModel.isInProgress.collectAsStateWithLifecycle()

    ItemDetailScreenComponent(
        state = state,
        isInProgress = isInProgress,
        onNavigateToOptions = viewModel::onNavigateToOptions,
        onBackPressed = viewModel::onBackPressed,
        onCastMemberClicked = viewModel::onCastMemberClicked,
        customContent = customContent,
        dateContent = dateContent,
    )
}

@Composable
fun <T : Item> ItemDetailScreenComponent(
    state: ItemDetailsViewModel.ViewState<T>,
    isInProgress: Boolean,
    onNavigateToOptions: () -> Unit,
    onBackPressed: () -> Unit,
    onCastMemberClicked: (Int) -> Unit,
    customContent: @Composable LazyItemScope.() -> Unit,
    dateContent: @Composable RowScope.() -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringOf(R.string.itemDetails_title),
                actions = {
                    BBIcon(
                        imageVector = Icons.Default.MoreVert,
                        modifier = Modifier.padding(end = 8.dp),
                        onClick = onNavigateToOptions
                    )
                },
                canNavigateBack = true,
                onBackPressed = onBackPressed
            )
        },

    ) { paddingValues ->
        Crossfade(
            targetState = isInProgress || state.details == null,
            label = "detailscreen"
        ) { isLoading ->
            if (isLoading) {
                LoadingShimmer(paddingValues)
            } else {
                DetailContent(paddingValues, state, onCastMemberClicked, dateContent, customContent)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun <T : Item> DetailContent(
    paddingValues: PaddingValues,
    state: ItemDetailsViewModel.ViewState<T>,
    onCastMemberClicked: (Int) -> Unit,
    dateContent: @Composable (RowScope.() -> Unit),
    customContent: @Composable (LazyItemScope.() -> Unit),
) {
    if (state.details == null) return
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ) {
        item {
            val item = state.details.displayedItem.item
            val isWatched = state.details.displayedItem.item.watchedDate != null
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .fillMaxWidth(0.5f)
                ) {
                    AsyncImage(
                        model = state.details.displayedItem.thumbnailUrl,
                        modifier = Modifier
                            .alpha(if (isWatched) .5f else 1f)
                            .fillMaxWidth(),
                        error = painterResource(id = Res.drawable.movie_placeholder),
                        placeholder = painterResource(id = Res.drawable.movie_placeholder),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                    )
                    if (isWatched) {
                        Icon(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(80.dp),
                            imageVector = Icons.Filled.Done,
                            contentDescription = null,
                            tint = BingeBotTheme.colors.highlight,
                        )
                        Text(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 8.dp),
                            text = item.watchedDate?.dateString ?: "",
                            style = MaterialTheme.typography.titleSmall,
                            color = BingeBotTheme.colors.highlight,
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (item.title != item.originalTitle) {
                        Text(
                            modifier = Modifier.padding(top = 16.dp),
                            text = "(${item.originalTitle})",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.voteAverage.asOneDecimalString,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        modifier = Modifier.padding(end = 8.dp),
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color.Yellow
                    )
                }
                dateContent()
            }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.primary
            )
            Row {
                Text(
                    text = stringResource(R.string.itemDetails_genre),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = state.details.genres.map { it.name }.joinToString(", "),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.primary
            )
            item.overview?.let { overview ->
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    text = overview,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            customContent()
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = stringResource(R.string.itemDetails_cast),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
                color = MaterialTheme.colorScheme.primary
            )
            LazyRow(modifier = Modifier.padding(bottom = 16.dp)) {
                items(state.details.credits.cast) { castMember ->
                    Column(
                        modifier = Modifier
                            .width(200.dp)
                            .padding(horizontal = 8.dp)
                            .clickable {
                                onCastMemberClicked(castMember.id)
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .fillMaxWidth()
                        ) {
                            AsyncImage(
                                model = castMember.profileUrl,
                                modifier = Modifier.fillMaxWidth(),
                                error = painterResource(id = Res.drawable.movie_placeholder),
                                placeholder = painterResource(id = Res.drawable.movie_placeholder),
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth
                            )
                        }
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = castMember.name,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = castMember.character,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
            if (state.details.providers.hasData) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.primary
                )
                if (!state.details.providers.flatrate.isEmpty()) {
                    WatchProviderSection(
                        title = stringResource(R.string.itemDetails_flatrate),
                        providers = state.details.providers.flatrate,
                    )
                }
                if (!state.details.providers.buy.isEmpty()) {
                    WatchProviderSection(
                        title = stringResource(R.string.itemDetails_buy),
                        providers = state.details.providers.buy,
                    )
                }
                if (!state.details.providers.rent.isEmpty()) {
                    WatchProviderSection(
                        title = stringResource(R.string.itemDetails_rent),
                        providers = state.details.providers.rent,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WatchProviderSection(
    title: String,
    providers: List<DisplayedProviderInfo>,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(end = 16.dp),
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
            ),
            color = MaterialTheme.colorScheme.primary
        )
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            providers.forEach { provider ->
                WatchProvider(provider.fullPath)
            }
        }
    }
}

@Composable
fun WatchProvider(
    logoUrl: String
) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = logoUrl,
            contentDescription = null,
        )
    }
}

@Composable
private fun LoadingShimmer(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxWidth(0.5f)
                    .aspectRatio(0.667f)
                    .shimmerEffect()
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmerEffect(),
                    text = "",
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmerEffect(),
                    text = "",
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(top = 16.dp)
                .clip(RoundedCornerShape(8.dp))
                .fillMaxWidth()
                .height(250.dp)
                .shimmerEffect()
        )
    }
}