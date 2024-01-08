package com.bingebot.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bingebot.splash.ui.SplashScreenComponent
import com.bingebot.ui.BingeBotTheme

@Composable
fun NavigationHost(
    navController: NavHostController = rememberNavController(),
) {
    BingeBotTheme {
        NavHost(navController = navController, startDestination = NavDestination.SPLASH.route) {
            composable(NavDestination.SPLASH.route) {
                SplashScreenComponent()
            }
        }
    }
}