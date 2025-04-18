package com.vadlevente.bingebot.core.ui.composables

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vadlevente.bingebot.core.util.dateString
import com.vadlevente.bingebot.resources.R
import com.vadlevente.bingebot.ui.BingeBotTheme
import java.util.Date

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListItem(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    title: String,
    iconPath: String?,
    watchedDate: Date? = null,
    rating: String,
    releaseYear: String?,
    info: String? = null,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    Crossfade(
        targetState = isLoading,
        label = "listitem"
    ) { isLoading ->
        if (isLoading) {
            LoadingShimmer(modifier)
        } else {
            ItemContent(
                watchedDate = watchedDate,
                modifier = modifier,
                onClick = onClick,
                onLongClick = onClick,
                iconPath = iconPath,
                title = title,
                releaseYear = releaseYear,
                rating = rating,
                info = info,
            )
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun ItemContent(
    watchedDate: Date?,
    modifier: Modifier,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    iconPath: String?,
    title: String,
    releaseYear: String?,
    rating: String,
    info: String?,
) {
    val isWatched = watchedDate != null
    Card(
        modifier = modifier
            .padding(8.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .width(100.dp)
                        .aspectRatio(1/1.5f)
                ) {
                    AsyncImage(
                        model = iconPath,
                        modifier = Modifier
                            .alpha(if (isWatched) .5f else 1f)
                            .fillMaxWidth(),
                        error = painterResource(id = R.drawable.movie_placeholder),
                        placeholder = painterResource(id = R.drawable.movie_placeholder),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds
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
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(horizontal = 18.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = title,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isWatched) BingeBotTheme.colors.highlight else MaterialTheme.colorScheme.primary,
                    )
                    releaseYear?.let {
                        Text(
                            text = releaseYear,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    info?.let {
                        Text(
                            text = info,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = rating,
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
            watchedDate?.let {
                Text(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 8.dp, end = 8.dp),
                    text = it.dateString,
                    style = MaterialTheme.typography.titleSmall,
                    color = BingeBotTheme.colors.highlight,
                )
            }
        }
    }
}

@Composable
private fun LoadingShimmer(modifier: Modifier) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .height(150.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = modifier
                .fillMaxHeight()
                .width(100.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmerEffect(),
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 18.dp)
                .weight(1f)
        ) {
            Text(
                modifier = Modifier
                    .width(300.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect(),
                text = "",
            )
            Text(
                modifier = Modifier
                    .width(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect(),
                text = "",
            )
        }
    }
}

@Preview
@Composable
private fun ListItemPreview() {
    BingeBotTheme {
        ListItem(
            isLoading = false,
            title = "Filmek",
            iconPath = "https://m.media-amazon.com/images/M/MV5BNGNhMDIzZTUtNTBlZi00MTRlLWFjM2ItYzViMjE3YzI5MjljXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg",
            watchedDate = Date(),
            rating = "8.9",
            releaseYear = "1994",
            info = "Író",
            onClick = { },
        )
    }
}