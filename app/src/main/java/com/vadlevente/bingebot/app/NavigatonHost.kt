package com.vadlevente.bingebot.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vadlevente.bingebot.authentication.ui.login.LoginScreen
import com.vadlevente.bingebot.authentication.ui.registration.RegistrationScreen
import com.vadlevente.bingebot.core.events.navigation.NavigationEvent.NavigateTo
import com.vadlevente.bingebot.core.events.navigation.NavigationEvent.NavigateUp
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.model.NavDestination
import com.vadlevente.bingebot.core.ui.composables.BBDialog
import com.vadlevente.bingebot.core.ui.composables.MovieBottomSheet
import com.vadlevente.bingebot.core.ui.composables.Toast
import com.vadlevente.bingebot.list.ui.MovieListScreen
import com.vadlevente.bingebot.search.ui.SearchMovieScreen
import com.vadlevente.bingebot.splash.ui.SplashScreen
import com.vadlevente.bingebot.ui.BingeBotTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun NavigationHost(
    navController: NavHostController = rememberNavController(),
    navigationEventChannel: NavigationEventChannel,
) {
    CollectEvents(
        navigationEventChannel,
        navController,
    )

    BingeBotTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            NavHost(navController = navController, startDestination = NavDestination.SPLASH.route) {
                composable(NavDestination.SPLASH.route) {
                    SplashScreen()
                }
                composable(NavDestination.REGISTRATION.route) {
                    RegistrationScreen()
                }
                composable(NavDestination.LOGIN.route) {
                    LoginScreen()
                }
                composable(NavDestination.LIST_MOVIE.route) {
                    MovieListScreen()
                }
                composable(NavDestination.SEARCH_MOVIE.route) {
                    SearchMovieScreen()
                }
            }
            Toast(
                modifier = Modifier.align(Alignment.TopCenter),
            )
            MovieBottomSheet()
            BBDialog()
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
            navigationEventChannel.events.collectLatest { event ->
                when (event) {
                    is NavigateTo -> navigate(navController, event.route)
                    NavigateUp -> navController.navigateUp()
                }
            }
        }
    }
}

private fun navigate(
    navController: NavHostController,
    route: String,
) {
    when (route) {
        NavDestination.SPLASH.route -> {
            navController.navigate(route) {
                popUpTo(NavDestination.SPLASH.route) {
                    inclusive = true
                }
            }
        }

        NavDestination.REGISTRATION.route,
        NavDestination.LOGIN.route,
        -> {
            if (navController.currentBackStackEntry?.destination?.route == NavDestination.SPLASH.route) {
                navController.popBackStack()
            }
            navController.navigate(route)
        }
        NavDestination.LIST_MOVIE.route -> {
            navController.popBackStack()
            navController.navigate(route)
        }

        else -> navController.navigate(route)
    }
}