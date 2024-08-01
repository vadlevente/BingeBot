package com.vadlevente.bingebot.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.core.model.SelectedLanguage
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.ui.composables.ProgressScreen
import com.vadlevente.bingebot.core.ui.composables.TopBar
import com.vadlevente.bingebot.settings.SettingsViewModel.ViewState
import com.vadlevente.bingebot.settings.ui.composables.SelectLanguageBottomSheet
import com.vadlevente.bingebot.ui.backgroundColor
import com.vadlevente.bingebot.ui.lightTextColor
import com.vadlevente.bingebot.ui.margin16
import com.vadlevente.bingebot.ui.settingsLabel
import com.vadlevente.bingebot.ui.settingsValue

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val isInProgress by viewModel.isInProgress.collectAsState()
    SettingsScreenComponent(
        state = state,
        isInProgress = isInProgress,
        onShowLanguageBottomSheet = viewModel::onShowLanguageBottomSheet,
        onDismissLanguageBottomSheet = viewModel::onDismissLanguageBottomSheet,
        onSelectLanguage = viewModel::onLanguageChanged,
        onLogout = viewModel::onLogout,
    )
}

@Composable
fun SettingsScreenComponent(
    state: ViewState,
    isInProgress: Boolean,
    onShowLanguageBottomSheet: () -> Unit,
    onDismissLanguageBottomSheet: () -> Unit,
    onSelectLanguage: (SelectedLanguage) -> Unit,
    onLogout: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringOf(R.string.settings_pageTitle),
                canNavigateBack = false,
            )
        },
    ) { paddingValues ->
        ProgressScreen(
            isProgressVisible = isInProgress,
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(margin16)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .clickable {
                            onShowLanguageBottomSheet()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Filled.Language,
                        tint = lightTextColor,
                        contentDescription = null,
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        text = stringResource(id = R.string.settings_selectLanguageLabel),
                        style = settingsLabel,
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        text = state.languages.entries.first { it.value }.key.displayName,
                        style = settingsValue,
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .clickable {
                            onLogout()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = AutoMirrored.Filled.Logout,
                        tint = lightTextColor,
                        contentDescription = null,
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        text = stringResource(id = R.string.settings_logout),
                        style = settingsLabel,
                    )
                }
            }
        }
    }
    if (state.showSelectLanguageBottomSheet) {
        SelectLanguageBottomSheet(
            languages = state.languages,
            onSelectLanguage = onSelectLanguage,
            onDismiss = onDismissLanguageBottomSheet,
        )
    }
}