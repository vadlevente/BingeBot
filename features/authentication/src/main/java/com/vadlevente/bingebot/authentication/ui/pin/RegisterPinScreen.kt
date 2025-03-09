package com.vadlevente.bingebot.authentication.ui.pin

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.authentication.R
import com.vadlevente.bingebot.authentication.ui.composables.PinScreen
import com.vadlevente.bingebot.core.stringOf
import com.vadlevente.bingebot.core.ui.composables.TopBar

@Composable
fun RegisterPinScreen(
    email: String,
    password: String,
) {
    val viewModel = hiltViewModel<RegisterPinViewModel, RegisterPinViewModel.RegisterPinViewModelFactory> { factory ->
        factory.create(email, password)
    }
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                canNavigateBack = true,
                onBackPressed = viewModel::onExitRegistration
            )
        }
    ) { paddingValues ->
        PinScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            title = stringOf(R.string.pin_register_title),
            pin = state.pin,
            onPinChanged = viewModel::onPinChanged
        )
    }
}