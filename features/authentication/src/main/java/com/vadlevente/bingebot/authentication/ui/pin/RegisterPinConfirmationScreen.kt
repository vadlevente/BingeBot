package com.vadlevente.bingebot.authentication.ui.pin

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vadlevente.bingebot.authentication.R
import com.vadlevente.bingebot.authentication.ui.composables.PinScreen
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.ui.composables.TopBar
import com.vadlevente.bingebot.ui.BingeBotTheme

@Composable
fun RegisterPinConfirmationScreen() {
    val viewModel: RegisterPinViewModel = viewModel(LocalContext.current as ComponentActivity)
    val state by viewModel.state.collectAsState()
    val isInProgress by viewModel.isInProgress.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopBar(
                    canNavigateBack = true,
                    onBackPressed = viewModel::onExitConfirmation
                )
            }
        ) { paddingValues ->
            PinScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                title = stringOf(R.string.pin_confirm_title),
                pin = state.pinConfirmed,
                onPinChanged = viewModel::onPinConfirmChanged
            )
        }
        if (isInProgress) {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.fillMaxSize().alpha(.5f)) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center),
                        color = BingeBotTheme.colors.highlight,
                    )
                }

            }
        }
    }
}