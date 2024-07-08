package com.vadlevente.bingebot.core.ui.composables

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.vadlevente.bingebot.core.UIText
import com.vadlevente.bingebot.core.asString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: UIText,
    canNavigateBack: Boolean = false,
    icons: @Composable RowScope.() -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(text = title.asString())
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = icons,
    )
}