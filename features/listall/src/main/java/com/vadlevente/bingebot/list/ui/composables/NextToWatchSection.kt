package com.vadlevente.bingebot.list.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vadlevente.bingebot.core.model.DisplayedItem
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.util.asOneDecimalString
import com.vadlevente.bingebot.core.util.yearString
import com.vadlevente.bingebot.list.R
import com.vadlevente.bingebot.ui.BingeBotTheme

@Composable
fun <T : Item> NextToWatchSection(
    modifier: Modifier = Modifier,
    items: List<DisplayedItem<T>>,
    onClickItem: (Int) -> Unit,
    onHideNextToWatch: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = stringResource(R.string.nextToWatch),
                style = MaterialTheme.typography.titleMedium,
                color = BingeBotTheme.colors.highlight,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.clickable {
                    onHideNextToWatch()
                },
                text = stringResource(R.string.disable_nextToWatch),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Card(
            modifier = Modifier
                .padding(top = 4.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            val pagerState = rememberPagerState(pageCount = { items.size })

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    val currentItem = items[page]
                    val item = currentItem.item
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onClickItem(item.id)
                            },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AsyncImage(
                            model = currentItem.thumbnailUrl,
                            error = painterResource(id = com.vadlevente.bingebot.resources.R.drawable.movie_placeholder),
                            placeholder = painterResource(id = com.vadlevente.bingebot.resources.R.drawable.movie_placeholder),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds
                        )
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 18.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = item.title,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.titleMedium,
                                color = BingeBotTheme.colors.highlight,
                            )
                            Text(
                                text = item.releaseDate?.yearString ?: "",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
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
                    }
                }

                PagerIndicator(
                    modifier = Modifier
                        .padding(end = 12.dp, bottom = 12.dp)
                        .align(Alignment.BottomEnd),
                    currentPage = pagerState.currentPage,
                    pageCount = items.size
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .padding(bottom = 4.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
private fun PagerIndicator(
    modifier: Modifier = Modifier,
    currentPage: Int,
    pageCount: Int = 3,
    activeColor: Color = BingeBotTheme.colors.highlight,
    inactiveColor: Color = MaterialTheme.colorScheme.background,
    dotSize: Dp = 10.dp,
    spacing: Dp = 8.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .clip(CircleShape)
                    .background(if (index == currentPage) activeColor else inactiveColor)
            )
            if (index != pageCount - 1) Spacer(modifier = Modifier.width(spacing))
        }
    }
}