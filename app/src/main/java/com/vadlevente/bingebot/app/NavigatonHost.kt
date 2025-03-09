package com.vadlevente.bingebot.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.navigation.toRoute
import com.vadlevente.bingebot.authentication.ui.authentication.AuthenticationScreen
import com.vadlevente.bingebot.authentication.ui.biometrics.RegisterBiometricsScreen
import com.vadlevente.bingebot.authentication.ui.login.LoginScreen
import com.vadlevente.bingebot.authentication.ui.pin.RegisterPinConfirmationScreen
import com.vadlevente.bingebot.authentication.ui.pin.RegisterPinScreen
import com.vadlevente.bingebot.authentication.ui.registration.RegistrationScreen
import com.vadlevente.bingebot.bottomsheet.ui.movie.AddMovieToWatchListBottomSheet
import com.vadlevente.bingebot.bottomsheet.ui.movie.MovieBottomSheet
import com.vadlevente.bingebot.bottomsheet.ui.movie.MovieOrderByBottomSheet
import com.vadlevente.bingebot.bottomsheet.ui.movie.MovieWatchListsBottomSheet
import com.vadlevente.bingebot.bottomsheet.ui.tv.AddTvToWatchListBottomSheet
import com.vadlevente.bingebot.bottomsheet.ui.tv.TvBottomSheet
import com.vadlevente.bingebot.bottomsheet.ui.tv.TvOrderByBottomSheet
import com.vadlevente.bingebot.bottomsheet.ui.tv.TvWatchListsBottomSheet
import com.vadlevente.bingebot.core.events.navigation.NavigationEvent.NavigateTo
import com.vadlevente.bingebot.core.events.navigation.NavigationEvent.NavigateUp
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.model.NavDestination
import com.vadlevente.bingebot.core.ui.composables.Toast
import com.vadlevente.bingebot.core.ui.composables.dialog.BBDialog
import com.vadlevente.bingebot.core.ui.composables.dialog.BBTextFieldDialog
import com.vadlevente.bingebot.dashboard.ui.DashboardScreen
import com.vadlevente.bingebot.search.ui.SearchMovieScreen
import com.vadlevente.bingebot.search.ui.SearchTvScreen
import com.vadlevente.bingebot.splash.ui.SplashScreen
import com.vadlevente.bingebot.ui.BingeBotTheme
import com.vadlevente.bingebot.watchlist.ui.movie.MovieWatchListScreen
import com.vadlevente.bingebot.watchlist.ui.tv.TvWatchListScreen
import com.vadlevente.moviedetails.ui.MovieDetailsScreen
import com.vadlevente.moviedetails.ui.TvDetailsScreen
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
            NavHost(navController = navController, startDestination = NavDestination.Splash) {
                composable<NavDestination.Splash> {
                    SplashScreen()
                }
                composable<NavDestination.Registration> {
                    RegistrationScreen()
                }
                composable<NavDestination.Login> {
                    LoginScreen()
                }
                composable<NavDestination.RegisterPin> {
                    val args: NavDestination.RegisterPin = it.toRoute()
                    RegisterPinScreen(args.email, args.password)
                }
                composable<NavDestination.RegisterPinConfirm> {
                    RegisterPinConfirmationScreen()
                }
                composable<NavDestination.Authenticate> {
                    AuthenticationScreen()
                }
                composable<NavDestination.BiometricsRegistration> {
                    val args: NavDestination.BiometricsRegistration = it.toRoute()
                    RegisterBiometricsScreen(args.email, args.password)
                }
                composable<NavDestination.Dashboard> {
                    DashboardScreen()
                }
                composable<NavDestination.SearchMovie> {
                    SearchMovieScreen()
                }
                composable<NavDestination.SearchTv> {
                    SearchTvScreen()
                }
                composable<NavDestination.MovieDetails> {
                    val args: NavDestination.MovieDetails = it.toRoute()
                    MovieDetailsScreen(args.movieId)
                }
                composable<NavDestination.TvDetails> {
                    val args: NavDestination.TvDetails = it.toRoute()
                    TvDetailsScreen(args.tvId)
                }
                composable<NavDestination.MovieWatchList> {
                    val args: NavDestination.MovieWatchList = it.toRoute()
                    MovieWatchListScreen(args.watchListId)
                }
                composable<NavDestination.TvWatchList> {
                    val args: NavDestination.TvWatchList = it.toRoute()
                    TvWatchListScreen(args.watchListId)
                }
            }
            UIComponents(this)
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
                    is NavigateTo -> navigate(navController, event.destination)
                    NavigateUp -> navController.popBackStack()
                    else -> {}
                }
            }
        }
    }
}

private fun navigate(
    navController: NavHostController,
    destination: NavDestination,
) {
    when (destination) {
        NavDestination.Splash,
        NavDestination.Registration,
        NavDestination.Login,
        NavDestination.Dashboard -> {
            navController.navigate(destination) {
                popUpTo(0)
            }
        }
        else -> navController.navigate(destination)
    }
}

@Composable
private fun UIComponents(scope: BoxScope) {
    with(scope) {
        Toast(
            modifier = Modifier.align(Alignment.TopCenter),
        )
        Dialogs()
        BottomSheets()
    }
}

@Composable
private fun Dialogs() {
    BBDialog()
    BBTextFieldDialog()
}

@Composable
private fun BottomSheets() {
    MovieBottomSheet()
    TvBottomSheet()
    AddMovieToWatchListBottomSheet()
    AddTvToWatchListBottomSheet()
    MovieWatchListsBottomSheet()
    TvWatchListsBottomSheet()
    MovieOrderByBottomSheet()
    TvOrderByBottomSheet()
}