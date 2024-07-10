package com.vadlevente.bingebot.core.ui.composables

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.vadlevente.bingebot.core.UIText
import com.vadlevente.bingebot.core.asString
import com.vadlevente.bingebot.ui.lightTextColor
import com.vadlevente.bingebot.ui.pageTitle
import com.vadlevente.bingebot.ui.toolbarColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: UIText,
    canNavigateBack: Boolean = false,
    actions: @Composable RowScope.() -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = title.asString(),
                style = pageTitle,
            )
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = lightTextColor,
                    )
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = toolbarColor,
        )
    )
}