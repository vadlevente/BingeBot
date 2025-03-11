package com.vadlevente.moviedetails.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.runtime.collectAsState
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
import com.vadlevente.bingebot.resources.R as Res

@Composable
fun <T : Item> ItemDetailsScreen(
    viewModel: ItemDetailsViewModel<T>,
    customContent: @Composable LazyItemScope.() -> Unit,
    dateContent: @Composable RowScope.() -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val isInProgress by viewModel.isInProgress.collectAsState()

    ItemDetailScreenComponent(
        state = state,
        isInProgress = isInProgress,
        onNavigateToOptions = viewModel::onNavigateToOptions,
        onBackPressed = viewModel::onBackPressed,
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
        if (isInProgress || state.details == null) {
            Column(
                modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .background(MaterialTheme.colorScheme.background)
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
            return@Scaffold
        }
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .background(MaterialTheme.colorScheme.background)
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
                            error = painterResource(id = Res.drawable.ic_placeholder),
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
                            .padding(start = 16.dp,)
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

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    text = item.overview,
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
                                .padding(horizontal = 8.dp),
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
                                    error = painterResource(id = Res.drawable.movie_poster_placeholder),
                                    placeholder = painterResource(id = Res.drawable.movie_poster_placeholder),
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
            }
        }
    }
}