package com.vadlevente.bingebot.common.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vadlevente.bingebot.ui.BingeBotTheme

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
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = BingeBotTheme.colors.highlight,
            labelColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.primary,
        ),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = MaterialTheme.colorScheme.onBackground,
            enabled = true,
            selected = isSelected,
        )
    )
}