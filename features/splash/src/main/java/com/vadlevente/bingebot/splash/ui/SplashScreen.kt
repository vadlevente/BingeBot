package com.vadlevente.bingebot.splash.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadlevente.bingebot.splash.SplashScreenViewModel
import com.vadlevente.bingebot.ui.backgroundColor
import com.vadlevente.bingebot.ui.progressColor

@Composable
fun SplashScreen(
    viewModel: SplashScreenViewModel = hiltViewModel(),
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = progressColor,
        )
    }
}