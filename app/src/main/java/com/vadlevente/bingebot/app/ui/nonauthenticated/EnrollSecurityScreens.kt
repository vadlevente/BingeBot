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
import com.vadlevente.bingebot.authentication.ui.pin.RegisterPinConfirmationScreen
import com.vadlevente.bingebot.authentication.ui.pin.RegisterPinScreen
import com.vadlevente.bingebot.core.events.navigation.NavigationEvent.EnrollSecurityNavigationEvent
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.model.NavDestination.EnrollSecurityNavDestination
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

@Composable
fun EnrollSecurityScreens(
    navigationEventChannel: NavigationEventChannel,
    email: String?,
    password: String?,
    canStepBack: Boolean,
    navController: NavHostController = rememberNavController(),
) {
    CollectEvents(
        navigationEventChannel,
        navController,
    )
    NavHost(
        navController = navController,
        startDestination = EnrollSecurityNavDestination.RegisterPin
    ) {
        composable<EnrollSecurityNavDestination.RegisterPin> {
            RegisterPinScreen(
                email = email ?: "",
                password = password ?: "",
                canStepBack = canStepBack,
            )
        }
        composable<EnrollSecurityNavDestination.RegisterPinConfirm> {
            RegisterPinConfirmationScreen()
        }
        composable<EnrollSecurityNavDestination.BiometricsRegistration> {
            val args: EnrollSecurityNavDestination.BiometricsRegistration = it.toRoute()
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
            navigationEventChannel.events.filterIsInstance<EnrollSecurityNavigationEvent>().collectLatest { event ->
                when (event) {
                    is EnrollSecurityNavigationEvent.NavigateTo -> navController.navigate(event.destination)
                    EnrollSecurityNavigationEvent.NavigateUp -> navController.popBackStack()
                    else -> {}
                }
            }
        }
    }
}