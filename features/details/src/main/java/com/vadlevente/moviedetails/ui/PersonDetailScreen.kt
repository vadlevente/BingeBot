package com.vadlevente.moviedetails.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.vadlevente.bingebot.core.asString
import com.vadlevente.bingebot.core.model.MediaType
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.ui.composables.ListItem
import com.vadlevente.bingebot.core.ui.composables.TopBar
import com.vadlevente.bingebot.core.ui.composables.shimmerEffect
import com.vadlevente.bingebot.core.util.asOneDecimalString
import com.vadlevente.bingebot.core.util.dateString
import com.vadlevente.bingebot.core.util.yearString
import com.vadlevente.bingebot.details.R
import com.vadlevente.moviedetails.PersonDetailsViewModel
import com.vadlevente.moviedetails.domain.model.DisplayedCreditRow
import com.vadlevente.bingebot.resources.R as Res

@Composable
fun PersonDetailsScreen(
    personId: Int,
) {
    val viewModel =
        hiltViewModel<PersonDetailsViewModel, PersonDetailsViewModel.PersonDetailsViewModelFactory> { factory ->
            factory.create(personId)
        }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isInProgress by viewModel.isInProgress.collectAsStateWithLifecycle()

    PersonDetailScreenComponent(
        state = state,
        isInProgress = isInProgress,
        onBackPressed = viewModel::onBackPressed,
        onMediaClicked = viewModel::onMediaClicked,
        toggleCastUpcomingHidden = viewModel::toggleCastUpcomingHidden,
        toggleCastPreviousHidden = viewModel::toggleCastPreviousHidden,
        toggleCrewUpcomingHidden = viewModel::toggleCrewUpcomingHidden,
        toggleCrewPreviousHidden = viewModel::toggleCrewPreviousHidden,
    )
}

@Composable
fun PersonDetailScreenComponent(
    state: PersonDetailsViewModel.ViewState,
    isInProgress: Boolean,
    onBackPressed: () -> Unit,
    onMediaClicked: (Int, MediaType) -> Unit,
    toggleCastUpcomingHidden: () -> Unit,
    toggleCastPreviousHidden: () -> Unit,
    toggleCrewUpcomingHidden: () -> Unit,
    toggleCrewPreviousHidden: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringOf(R.string.itemDetails_title),
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
                DetailContent(
                    paddingValues = paddingValues,
                    state = state,
                    onMediaClicked = onMediaClicked,
                    toggleCastUpcomingHidden = toggleCastUpcomingHidden,
                    toggleCastPreviousHidden = toggleCastPreviousHidden,
                    toggleCrewUpcomingHidden = toggleCrewUpcomingHidden,
                    toggleCrewPreviousHidden = toggleCrewPreviousHidden,
                )
            }
        }
    }
}

@Composable
private fun DetailContent(
    paddingValues: PaddingValues,
    state: PersonDetailsViewModel.ViewState,
    onMediaClicked: (Int, MediaType) -> Unit,
    toggleCastUpcomingHidden: () -> Unit,
    toggleCastPreviousHidden: () -> Unit,
    toggleCrewUpcomingHidden: () -> Unit,
    toggleCrewPreviousHidden: () -> Unit,
) {
    if (state.details == null) return
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ) {
        item {
            val details = state.details
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
                        model = details.profileUrl,
                        modifier = Modifier.fillMaxWidth(),
                        error = painterResource(id = Res.drawable.movie_placeholder),
                        placeholder = painterResource(id = Res.drawable.movie_placeholder),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = details.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        modifier = Modifier.padding(top = 16.dp),
                        text = "${details.birthDay?.dateString ?: ""}-${details.deathDay?.dateString ?: ""}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        items(
            items = state.details.rows,
            key = { it.id }
        ) { row ->
            when (row) {
                is DisplayedCreditRow.DisplayedCreditHeader -> {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        text = row.title.asString(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                is DisplayedCreditRow.DisplayedCreditSectionHeader -> {
                    ListHeader(
                        isHidden = when {
                            row.isCast && row.isUpcoming -> state.castUpcomingHidden
                            row.isCast && !row.isUpcoming -> state.castPreviousHidden
                            !row.isCast && row.isUpcoming -> state.crewUpcomingHidden
                            else -> state.crewPreviousHidden
                        },
                        text = row.title.asString(),
                        onClick =
                        when {
                            row.isCast && row.isUpcoming -> toggleCastUpcomingHidden
                            row.isCast && !row.isUpcoming -> toggleCastPreviousHidden
                            !row.isCast && row.isUpcoming -> toggleCrewUpcomingHidden
                            else -> toggleCrewPreviousHidden
                        }
                    )
                }
                is DisplayedCreditRow.DisplayedCredit -> {
                    val credit = row.credit
                    ListItem(
                        modifier = Modifier.animateItem(
                            fadeInSpec = spring(stiffness = Spring.StiffnessMediumLow),
                            placementSpec = spring(stiffness = Spring.StiffnessLow),
                            fadeOutSpec = spring(stiffness = Spring.StiffnessMediumLow),
                        ),
                        isLoading = false,
                        title = credit.title,
                        iconPath = credit.posterPath,
                        rating = credit.voteAverage.asOneDecimalString,
                        releaseYear = credit.releaseDate?.yearString,
                        info = credit.info,
                        onClick = {
                            onMediaClicked(credit.id, credit.mediaType)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LazyItemScope.ListHeader(
    isHidden: Boolean,
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(shape = RoundedCornerShape(8.dp))
            .clickable {
                onClick()
            }
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier,
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.weight(1f))
        val rotation by animateFloatAsState(if (isHidden) 0f else 180f, label = "")
        Icon(
            modifier = Modifier.graphicsLayer { rotationZ = rotation },
            imageVector = Icons.Filled.KeyboardArrowDown,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
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