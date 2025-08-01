package com.vadlevente.bingebot.authentication.ui.authentication

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vadlevente.bingebot.authentication.R
import com.vadlevente.bingebot.authentication.ui.composables.PinScreen
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.ui.composables.BBBiometricPrompt
import com.vadlevente.bingebot.core.ui.composables.TopBar
import com.vadlevente.bingebot.ui.BingeBotTheme
import kotlinx.coroutines.launch
import com.vadlevente.bingebot.resources.R as Res

@Composable
fun AuthenticationScreen(
    viewModel: AuthenticationViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isInProgress by viewModel.isInProgress.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopBar(
                    canNavigateBack = false,
                )
            }
        ) { paddingValues ->
            PinScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                title = stringOf(R.string.pin_authentication_title),
                pin = state.pin,
                showBiometrics = state.isBiometricsEnrolled,
                onPinChanged = viewModel::onPinChanged,
                onFingerprintClicked = viewModel::onOpenBiometricAuthentication,
            )
        }
        if (isInProgress) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background.copy(alpha = .7f)
            ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                            .size(80.dp)
                            .align(Alignment.Center),
                        color = BingeBotTheme.colors.highlight,
                    )
            }
        }
    }

    if (state.showBiometricPrompt && state.cipher != null) {
        BBBiometricPrompt(
            title = stringResource(R.string.biometrics_registration_dialogTitle),
            negativeButtonText = stringResource(Res.string.common_Cancel),
            cipher = state.cipher!!,
            onAuthSuccessful = viewModel::onBiometricAuthSuccessful,
            onAuthDismissed = viewModel::onBiometricPromptCancelled,
        )
    }
    BackHandler {
        coroutineScope.launch {
            viewModel.showExitConfirmation()
        }
    }
}