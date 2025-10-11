package com.vadlevente.bingebot.app.ui.nonauthenticated

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vadlevente.bingebot.authentication.ui.login.LoginScreen
import com.vadlevente.bingebot.authentication.ui.registration.RegistrationScreen
import com.vadlevente.bingebot.core.events.navigation.NavigationEvent.OnboardingNavigationEvent
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.model.NavDestination.OnboardingNavDestination
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreens(
    navigationEventChannel: NavigationEventChannel,
    navController: NavHostController = rememberNavController(),
) {
    CollectEvents(
        navigationEventChannel,
        navController,
    )
    NavHost(
        navController = navController,
        startDestination = OnboardingNavDestination.Login
    ) {
        composable<OnboardingNavDestination.Registration> {
            RegistrationScreen()
        }
        composable<OnboardingNavDestination.Login> {
            LoginScreen()
        }
    }
}

@Composable
private fun CollectEvents(
    navigationEventChannel: NavigationEventChannel,
    navController: NavHostController,
) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(true) {
        coroutineScope.launch {
            navigationEventChannel.events.filterIsInstance<OnboardingNavigationEvent>().collectLatest { event ->
                when (event) {
                    is OnboardingNavigationEvent.NavigateTo -> navigate(navController, event.destination)
                    OnboardingNavigationEvent.NavigateUp -> navController.popBackStack()
                    else -> {}
                }
            }
        }
    }
}

private fun navigate(
    navController: NavHostController,
    destination: OnboardingNavDestination,
) {
    when (destination) {
        OnboardingNavDestination.Registration,
        OnboardingNavDestination.Login -> {
            navController.navigate(destination) {
                popUpTo(0)
            }
        }
        else -> navController.navigate(destination)
    }
}