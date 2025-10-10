package com.vadlevente.moviedetails.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vadlevente.bingebot.core.util.yearString
import com.vadlevente.bingebot.details.R
import com.vadlevente.moviedetails.TvDetailsViewModel

@Composable
fun TvDetailsScreen(
    tvId: Int,
) {
    val viewModel =
        hiltViewModel<TvDetailsViewModel, TvDetailsViewModel.TvDetailsViewModelFactory> { factory ->
            factory.create(tvId)
        }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val tv = state.details?.displayedItem?.item ?: return
    ItemDetailsScreen(
        viewModel = viewModel,
        customContent = {
            tv.creator?.let { creator ->
                Row {
                    Text(
                        text = stringResource(R.string.itemDetails_creator),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = creator,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
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
                    text = stringResource(R.string.itemDetails_numberOfSeasons),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = tv.numberOfSeasons.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
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
                    text = stringResource(R.string.itemDetails_numberOfEpisodes),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = tv.numberOfEpisodes.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dateContent = {
            Text(
                text = if (tv.releaseDate?.yearString != null && tv.releaseDate?.yearString == tv.lastAirDate?.yearString)
                    tv.releaseDate?.yearString ?: ""
                else
                    "${tv.releaseDate?.yearString ?: ""}-${tv.lastAirDate?.yearString ?: ""}",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    )
}