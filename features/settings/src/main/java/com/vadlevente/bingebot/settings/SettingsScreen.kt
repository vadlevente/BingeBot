package com.vadlevente.bingebot.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vadlevente.bingebot.core.model.SelectedLanguage
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.ui.composables.BBIcon
import com.vadlevente.bingebot.core.ui.composables.ProgressScreen
import com.vadlevente.bingebot.core.ui.composables.TopBar
import com.vadlevente.bingebot.settings.SettingsViewModel.ViewState
import com.vadlevente.bingebot.settings.ui.composables.SelectLanguageBottomSheet
import com.vadlevente.bingebot.ui.BingeBotTheme
import com.vadlevente.bingebot.ui.margin16
import com.vadlevente.bingebot.resources.R as Res

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isInProgress by viewModel.isInProgress.collectAsStateWithLifecycle()
    SettingsScreenComponent(
        state = state,
        isInProgress = isInProgress,
        onShowLanguageBottomSheet = viewModel::onShowLanguageBottomSheet,
        onDismissLanguageBottomSheet = viewModel::onDismissLanguageBottomSheet,
        onSelectLanguage = viewModel::onLanguageChanged,
        onLogout = viewModel::onLogout,
        onSecurityChanged = viewModel::onSecurityChanged,
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
    onSecurityChanged: (Boolean) -> Unit,
) {
    BackHandler(enabled = state.isSyncInProgress) {  }
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
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(margin16)
            ) {
                state.email?.let { email ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        BBIcon(
                            imageVector = Filled.Person,
                        )
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 16.dp),
                            text = stringResource(id = R.string.settings_user),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 16.dp),
                            text = email,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .clickable {
                            onShowLanguageBottomSheet()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BBIcon(
                        imageVector = Filled.Language,
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        text = stringResource(id = R.string.settings_selectLanguageLabel),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        text = state.languages.entries.first { it.value }.key.displayName,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
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
                    BBIcon(
                        imageVector = AutoMirrored.Filled.Logout,
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        text = stringResource(id = R.string.settings_logout),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .clickable {
                            onSecurityChanged(!state.isSecurityOn)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BBIcon(
                        imageVector = Filled.Security,
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        text = stringResource(id = R.string.settings_security),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Switch(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable(enabled = false) {},
                        checked = state.isSecurityOn,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                            uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                            uncheckedTrackColor = MaterialTheme.colorScheme.surface,
                            checkedBorderColor = MaterialTheme.colorScheme.primary,
                            uncheckedBorderColor = MaterialTheme.colorScheme.primary,
                            disabledCheckedThumbColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
                            disabledUncheckedThumbColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)
                        ),
                        onCheckedChange = {},
                    )
                }
            }
        }
    }
    if (state.isSyncInProgress) {
        Box(
            modifier = Modifier
                .fillMaxSize().background(MaterialTheme.colorScheme.background.copy(alpha = .7f))
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center)
                        .size(80.dp),
                    color = BingeBotTheme.colors.highlight,
                )
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = stringResource(Res.string.syncContent),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
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