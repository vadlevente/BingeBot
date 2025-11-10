package com.vadlevente.bingebot.authentication.ui.pin

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vadlevente.bingebot.authentication.R
import com.vadlevente.bingebot.authentication.ui.composables.PinScreen
import com.vadlevente.bingebot.common.ui.composables.TopBar
import com.vadlevente.bingebot.core.ui.stringOf

@Composable
fun RegisterPinScreen(
    email: String,
    password: String,
    canStepBack: Boolean,
) {
    val activity = LocalContext.current as ComponentActivity
    val viewModel = hiltViewModel<RegisterPinViewModel, RegisterPinViewModel.RegisterPinViewModelFactory>(viewModelStoreOwner = activity) { factory ->
        factory.create(email, password)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopBar(
                canNavigateBack = canStepBack,
                onBackPressed = if (canStepBack) viewModel::onCloseFlow else viewModel::onExitRegistration,
            )
        }
    ) { paddingValues ->
        PinScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            title = stringOf(R.string.pin_register_title),
            pin = state.pin,
            showBiometrics = false,
            onPinChanged = viewModel::onPinChanged
        )
    }
    BackHandler {
        if (!canStepBack) {
            viewModel.onExitRegistration()
        } else {
            viewModel.onCloseFlow()
        }
    }
}