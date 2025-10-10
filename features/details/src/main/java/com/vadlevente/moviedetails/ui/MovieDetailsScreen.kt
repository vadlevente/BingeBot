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
import com.vadlevente.bingebot.core.util.asDollarAmount
import com.vadlevente.bingebot.core.util.yearString
import com.vadlevente.bingebot.details.R
import com.vadlevente.moviedetails.MovieDetailsViewModel

@Composable
fun MovieDetailsScreen(
    movieId: Int,
) {
    val viewModel =
        hiltViewModel<MovieDetailsViewModel, MovieDetailsViewModel.MovieDetailsViewModelFactory> { factory ->
            factory.create(movieId)
        }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val movie = state.details?.displayedItem?.item ?: return
    val credits = state.details?.credits ?: return
    ItemDetailsScreen(
        viewModel = viewModel,
        customContent = {
            if (credits.director.isNotEmpty()) {
                Row {
                    Text(
                        text = stringResource(R.string.itemDetails_director),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = credits.director.map { it.name }.joinToString(", "),
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
            if (credits.writer.isNotEmpty()) {
                Row {
                    Text(
                        text = stringResource(R.string.itemDetails_writer),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = credits.writer.map { it.name }.joinToString(", "),
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
            movie.budget?.let { budget ->
                Row {
                    Text(
                        text = stringResource(R.string.itemDetails_budget),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = budget.asDollarAmount,
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
            }
            movie.revenue?.let { revenue ->
                Row {
                    Text(
                        text = stringResource(R.string.itemDetails_revenue),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = revenue.asDollarAmount,
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
            }
            movie.runtime?.let { runtime ->
                Row {
                    Text(
                        text = stringResource(R.string.itemDetails_runtime),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = stringResource(R.string.itemDetails_runtimeFormatted, runtime/60, runtime%60),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        dateContent = {
            Text(
                text = movie.releaseDate?.yearString ?: "",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    )
}
