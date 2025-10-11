package com.vadlevente.bingebot.app

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
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
import com.vadlevente.bingebot.app.ui.authenticated.AuthenticatedScreens
import com.vadlevente.bingebot.app.ui.nonauthenticated.EnrollSecurityScreens
import com.vadlevente.bingebot.app.ui.nonauthenticated.OnboardingScreens
import com.vadlevente.bingebot.authentication.ui.authentication.AuthenticationScreen
import com.vadlevente.bingebot.bottomsheet.ui.movie.AddMovieToWatchListBottomSheet
import com.vadlevente.bingebot.bottomsheet.ui.movie.MovieBottomSheet
import com.vadlevente.bingebot.bottomsheet.ui.movie.MovieOrderByBottomSheet
import com.vadlevente.bingebot.bottomsheet.ui.movie.MovieWatchListsBottomSheet
import com.vadlevente.bingebot.bottomsheet.ui.tv.AddTvToWatchListBottomSheet
import com.vadlevente.bingebot.bottomsheet.ui.tv.TvBottomSheet
import com.vadlevente.bingebot.bottomsheet.ui.tv.TvOrderByBottomSheet
import com.vadlevente.bingebot.bottomsheet.ui.tv.TvWatchListsBottomSheet
import com.vadlevente.bingebot.core.events.navigation.NavigationEvent.TopNavigationEvent
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.model.NavDestination.TopNavDestination
import com.vadlevente.bingebot.core.ui.composables.Toast
import com.vadlevente.bingebot.core.ui.composables.dialog.BBDialog
import com.vadlevente.bingebot.core.ui.composables.dialog.BBTextFieldDialog
import com.vadlevente.bingebot.splash.ui.SplashScreen
import com.vadlevente.bingebot.ui.BingeBotTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
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
            NavHost(
                navController = navController,
                startDestination = TopNavDestination.Splash
            ) {
                composable<TopNavDestination.Splash> {
                    SplashScreen()
                }
                composable<TopNavDestination.Onboarding> {
                    OnboardingScreens(navigationEventChannel)
                }
                composable<TopNavDestination.EnrollSecurity> {
                    val args: TopNavDestination.EnrollSecurity = it.toRoute()
                    EnrollSecurityScreens(
                        navigationEventChannel = navigationEventChannel,
                        email = args.email,
                        password = args.password,
                        canStepBack = args.canStepBack,
                    )
                }
                composable<TopNavDestination.AuthenticatedScreens> {
                    AuthenticatedScreens(navigationEventChannel)
                }
                composable<TopNavDestination.Authenticate>(
                    enterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            tween(700),
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            tween(700),
                        )
                    }
                ) {
                    AuthenticationScreen()
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
            navigationEventChannel.events.filterIsInstance<TopNavigationEvent>()
                .collectLatest { event ->
                    when (event) {
                        is TopNavigationEvent.NavigateTo -> {
                            navController.navigate(event.destination) {
                                if (
                                    event.destination != TopNavDestination.Authenticate &&
                                    event.destination !is TopNavDestination.EnrollSecurity
                                ) {
                                    popUpTo(0)
                                }
                            }
                        }
                        TopNavigationEvent.NavigateUp -> navController.popBackStack()

                        else -> {}
                    }
                }
        }
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