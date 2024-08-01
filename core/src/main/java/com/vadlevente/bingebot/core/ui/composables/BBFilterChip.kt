package com.vadlevente.bingebot.core.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vadlevente.bingebot.ui.darkTextColor
import com.vadlevente.bingebot.ui.lightTextColor
import com.vadlevente.bingebot.ui.onBackgroundColor
import com.vadlevente.bingebot.ui.progressColor
import com.vadlevente.bingebot.ui.selectedChipLabel
import com.vadlevente.bingebot.ui.unselectedChipLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BBFilterChip(
    title: String,
    isSelected: Boolean,
    onClicked: () -> Unit,
) {
    FilterChip(
        modifier = Modifier.padding(horizontal = 4.dp),
        selected = isSelected,
        onClick = { onClicked() },
        label = {
            Text(
                text = title,
                style = if (isSelected) selectedChipLabel else unselectedChipLabel
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = progressColor,
            labelColor = lightTextColor,
            selectedLabelColor = darkTextColor,
        ),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = onBackgroundColor,
            enabled = true,
            selected = isSelected,
        )
    )
}