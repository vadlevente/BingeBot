package com.vadlevente.bingebot.core.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vadlevente.bingebot.core.UIText
import com.vadlevente.bingebot.core.asString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: UIText? = null,
    canNavigateBack: Boolean = false,
    actions: @Composable RowScope.() -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    Box(modifier = Modifier.height(70.dp)) {
        TopAppBar(
            modifier = Modifier.align(Alignment.Center),
            title = {
                title?.let {
                    Text(
                        text = title.asString(),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            },
            navigationIcon = {
                if (canNavigateBack) {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            },
            actions = actions,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
            )
        )
    }
}