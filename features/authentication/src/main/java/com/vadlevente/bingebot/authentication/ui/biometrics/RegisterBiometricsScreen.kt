package com.vadlevente.bingebot.authentication.ui.biometrics

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vadlevente.bingebot.authentication.R
import com.vadlevente.bingebot.common.ui.composables.BBBiometricPrompt
import com.vadlevente.bingebot.common.ui.composables.BBButton
import com.vadlevente.bingebot.common.ui.composables.BBOutlinedButton
import com.vadlevente.bingebot.ui.BingeBotTheme
import kotlinx.coroutines.launch
import javax.crypto.Cipher
import com.vadlevente.bingebot.resources.R as Res

@Composable
fun RegisterBiometricsScreen(
    email: String,
    password: String,
) {
    val coroutineScope = rememberCoroutineScope()
    val viewModel = hiltViewModel<RegisterBiometricsViewModel, RegisterBiometricsViewModel.RegisterBiometricsViewModelFactory> { factory ->
        factory.create(email, password)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isInProgress by viewModel.isInProgress.collectAsStateWithLifecycle()
    RegisterBiometricsScreenComponent(
        isInProgress = isInProgress,
        viewState = state,
        onCancel = viewModel::onCancel,
        onConfirm = viewModel::onConfirm,
        onAuthSuccessful = viewModel::onAuthSuccessful,
        onAuthDismissed = viewModel::onAuthDismissed,
    )
    BackHandler {
        coroutineScope.launch {
            viewModel.showExitConfirmation()
        }
    }
}

@Composable
fun RegisterBiometricsScreenComponent(
    isInProgress: Boolean,
    viewState: RegisterBiometricsViewModel.ViewState,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    onAuthSuccessful: (Cipher) -> Unit,
    onAuthDismissed: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.weight(2f))
            Text(
                text = stringResource(R.string.biometrics_registration_description),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(4f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BBOutlinedButton(
                    text = stringResource(Res.string.common_No),
                    onClick = onCancel
                )
                BBButton(
                    text = stringResource(Res.string.common_Yes),
                    onClick = onConfirm
                )
            }
            Spacer(modifier = Modifier.weight(4f))
        }

        if (viewState.showBiometricPrompt && viewState.cipher != null) {
            BBBiometricPrompt(
                title = stringResource(R.string.biometrics_registration_dialogTitle),
                negativeButtonText = stringResource(Res.string.common_Cancel),
                cipher = viewState.cipher,
                onAuthSuccessful = onAuthSuccessful,
                onAuthDismissed = onAuthDismissed
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
}

