package com.vadlevente.bingebot.authentication.ui.authentication

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.authentication.R
import com.vadlevente.bingebot.authentication.ui.composables.PinScreen
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.ui.composables.BBBiometricPrompt
import com.vadlevente.bingebot.core.ui.composables.TopBar
import com.vadlevente.bingebot.resources.R as Res

@Composable
fun AuthenticationScreen(
    viewModel: AuthenticationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                canNavigateBack = true,
                onBackPressed = viewModel::onExitAuthentication
            )
        }
    ) { paddingValues ->
        PinScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            title = stringOf(R.string.pin_authentication_title),
            pin = state.pin,
            onPinChanged = viewModel::onPinChanged
        )
    }

    if (state.showBiometricPrompt && state.cipher != null) {
        BBBiometricPrompt(
            title = stringResource(R.string.biometrics_registration_dialogTitle),
            negativeButtonText = stringResource(Res.string.common_Cancel),
            cipher = state.cipher!!,
            onAuthSuccessful = viewModel::onBiometricAuthSuccessful,
            onAuthCancelled = viewModel::onBiometricPromptCancelled
        )
    }
}