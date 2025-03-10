package com.vadlevente.bingebot.core.ui.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vadlevente.bingebot.ui.BingeBotTheme
import java.util.Date

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListItemSmall(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    title: String,
    watchedDate: Date? = null,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    if (isLoading) {
        Box(
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth()
                .shimmerEffect()
        )
        return
    }
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
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                    ,
                    text = title,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isWatched) BingeBotTheme.colors.highlight else MaterialTheme.colorScheme.primary,
                )
                if (isWatched) {
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                        imageVector = Icons.Filled.Done,
                        contentDescription = null,
                        tint = BingeBotTheme.colors.highlight,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ListItemSmallPreview() {
    BingeBotTheme {
        ListItemSmall(
            isLoading = false,
            title = "Filmek",
            watchedDate = Date(),
            onClick = { },
        )
    }
}