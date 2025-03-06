package com.vadlevente.bingebot.authentication.ui.biometrics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.authentication.R
import com.vadlevente.bingebot.core.ui.composables.BBBiometricPrompt
import com.vadlevente.bingebot.core.ui.composables.BBButton
import com.vadlevente.bingebot.core.ui.composables.BBOutlinedButton
import javax.crypto.Cipher
import com.vadlevente.bingebot.resources.R as Res

@Composable
fun RegisterBiometricsScreen(
    viewModel: RegisterBiometricsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    RegisterBiometricsScreenComponent(
        viewState = state,
        onCancel = viewModel::onCancel,
        onConfirm = viewModel::onConfirm,
        onAuthSuccessful = viewModel::onAuthSuccessful,
        onAuthCancelled = viewModel::onCancel
    )
}

@Composable
fun RegisterBiometricsScreenComponent(
    viewState: RegisterBiometricsViewModel.ViewState,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    onAuthSuccessful: (Cipher) -> Unit,
    onAuthCancelled: () -> Unit
) {
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
            onAuthCancelled = onAuthCancelled
        )
    }
}

