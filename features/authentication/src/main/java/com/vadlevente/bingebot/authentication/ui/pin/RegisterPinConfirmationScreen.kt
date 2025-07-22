package com.vadlevente.bingebot.authentication.ui.pin

import androidx.activity.ComponentActivity
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vadlevente.bingebot.authentication.R
import com.vadlevente.bingebot.authentication.ui.composables.PinScreen
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.ui.composables.TopBar
import com.vadlevente.bingebot.ui.BingeBotTheme
import kotlinx.coroutines.launch

@Composable
fun RegisterPinConfirmationScreen() {
    val coroutineScope = rememberCoroutineScope()
    val viewModel: RegisterPinViewModel = viewModel(LocalContext.current as ComponentActivity)
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isInProgress by viewModel.isInProgress.collectAsStateWithLifecycle()
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopBar(
                    canNavigateBack = true,
                    onBackPressed = viewModel::onExitConfirmation,
                )
            }
        ) { paddingValues ->
            PinScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                title = stringOf(R.string.pin_confirm_title),
                pin = state.pinConfirmed,
                showBiometrics = false,
                onPinChanged = viewModel::onPinConfirmChanged
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
        BackHandler {
            coroutineScope.launch {
                viewModel.onExitConfirmation()
            }
        }
    }
}