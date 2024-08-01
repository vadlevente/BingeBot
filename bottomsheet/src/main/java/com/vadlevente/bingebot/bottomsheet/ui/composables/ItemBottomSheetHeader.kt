package com.vadlevente.bingebot.bottomsheet.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vadlevente.bingebot.ui.listItemSubtitle
import com.vadlevente.bingebot.ui.listItemTitle
import com.vadlevente.bingebot.resources.R as Resources

@Composable
fun ItemBottomSheetHeader(
    thumbnailUrl: String?,
    title: String,
    releaseYear: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = thumbnailUrl,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .width(60.dp),
            placeholder = painterResource(id = Resources.drawable.movie_poster_placeholder),
            error = painterResource(id = Resources.drawable.movie_poster_placeholder),
            contentDescription = null,
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp)
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
    }
}