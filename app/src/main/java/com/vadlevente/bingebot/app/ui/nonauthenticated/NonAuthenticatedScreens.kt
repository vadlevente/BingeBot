package com.vadlevente.bingebot.app.ui.nonauthenticated

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.vadlevente.bingebot.authentication.ui.biometrics.RegisterBiometricsScreen
import com.vadlevente.bingebot.authentication.ui.login.LoginScreen
import com.vadlevente.bingebot.authentication.ui.pin.RegisterPinConfirmationScreen
import com.vadlevente.bingebot.authentication.ui.pin.RegisterPinScreen
import com.vadlevente.bingebot.authentication.ui.registration.RegistrationScreen
import com.vadlevente.bingebot.core.events.navigation.NavigationEvent.NonAuthenticatedNavigationEvent
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.model.NavDestination.NonAuthenticatedNavDestination
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

@Composable
fun NonAuthenticatedScreens(
    navigationEventChannel: NavigationEventChannel,
    registerPin: Boolean,
    navController: NavHostController = rememberNavController(),
) {
    CollectEvents(
        navigationEventChannel,
        navController,
    )
    NavHost(
        navController = navController,
        startDestination = if (registerPin) NonAuthenticatedNavDestination.RegisterPin("", "") else NonAuthenticatedNavDestination.Login
    ) {

        composable<NonAuthenticatedNavDestination.Registration> {
            RegistrationScreen()
        }
        composable<NonAuthenticatedNavDestination.Login> {
            LoginScreen()
        }
        composable<NonAuthenticatedNavDestination.RegisterPin> {
            val args: NonAuthenticatedNavDestination.RegisterPin = it.toRoute()
            RegisterPinScreen(args.email, args.password)
        }
        composable<NonAuthenticatedNavDestination.RegisterPinConfirm> {
            RegisterPinConfirmationScreen()
        }
        composable<NonAuthenticatedNavDestination.BiometricsRegistration> {
            val args: NonAuthenticatedNavDestination.BiometricsRegistration = it.toRoute()
            RegisterBiometricsScreen(args.email, args.password)
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
            navigationEventChannel.events.filterIsInstance<NonAuthenticatedNavigationEvent>().collectLatest { event ->
                when (event) {
                    is NonAuthenticatedNavigationEvent.NavigateTo -> navigate(navController, event.destination)
                    NonAuthenticatedNavigationEvent.NavigateUp -> navController.popBackStack()
                    else -> {}
                }
            }
        }
    }
}

private fun navigate(
    navController: NavHostController,
    destination: NonAuthenticatedNavDestination,
) {
    when (destination) {
        NonAuthenticatedNavDestination.Registration,
        NonAuthenticatedNavDestination.Login,
        is NonAuthenticatedNavDestination.RegisterPin -> {
            navController.navigate(destination) {
                popUpTo(0)
            }
        }
        else -> navController.navigate(destination)
    }
}