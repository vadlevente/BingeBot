package com.vadlevente.bingebot.core.ui.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vadlevente.bingebot.core.R
import com.vadlevente.bingebot.ui.BingeBotTheme
import com.vadlevente.bingebot.ui.cardColor
import com.vadlevente.bingebot.ui.listItemSubtitle
import com.vadlevente.bingebot.ui.listItemTitle

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListItem(
    title: String,
    iconPath: String?,
    isWatched: Boolean,
    rating: String,
    releaseYear: String,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    onDelete: () -> Unit = {},
    onAddToList: () -> Unit = {},
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor,
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = iconPath,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .width(100.dp),
                placeholder = painterResource(id = R.drawable.movie_poster_placeholder),
                error = painterResource(id = R.drawable.movie_poster_placeholder),
                contentDescription = null,
            )
            Column(modifier = Modifier
                .padding(horizontal = 18.dp)
                .weight(1f)
            ) {
                Text(
                    text = title,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = listItemTitle,
                )
                Text(
                    text = releaseYear,
                    style = listItemSubtitle,
                )
            }
            Row {
                Text(
                    text = rating,
                    style = listItemSubtitle,
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
}

@Preview
@Composable
private fun ListItemPreview() {
    BingeBotTheme {
        ListItem(
            title = "Filmek",
            iconPath = "https://m.media-amazon.com/images/M/MV5BNGNhMDIzZTUtNTBlZi00MTRlLWFjM2ItYzViMjE3YzI5MjljXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg",
            isWatched = true,
            rating = "8.9",
            releaseYear = "1994",
            onClick = {  },
            onDelete = {  },
            onAddToList = {},
        )
    }
}