package com.vadlevente.bingebot.dashboard.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vadlevente.bingebot.core.model.NavDestination
import com.vadlevente.bingebot.core.model.NavDestination.LIST_MOVIE
import com.vadlevente.bingebot.dashboard.R
import com.vadlevente.bingebot.list.ui.MovieListScreen
import com.vadlevente.bingebot.list.ui.TvListScreen
import com.vadlevente.bingebot.settings.SettingsScreen
import com.vadlevente.bingebot.ui.cardColor
import com.vadlevente.bingebot.ui.darkNavItemColor
import com.vadlevente.bingebot.ui.lightNavItemColor
import com.vadlevente.bingebot.ui.navigationBarItemTitleSelected
import com.vadlevente.bingebot.ui.navigationBarItemTitleUnselected

@Composable
fun DashboardScreen(
    navController: NavHostController = rememberNavController(),
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = NavDestination.LIST_MOVIE.route
        ) {
            composable(NavDestination.LIST_MOVIE.route) {
                MovieListScreen()
            }
            composable(NavDestination.LIST_TV.route) {
                TvListScreen()
            }
            composable(NavDestination.SETTINGS.route) {
                SettingsScreen()
            }
        }
    }

}

@Composable
fun BottomNavigationBar(navController: NavController) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    navController.addOnDestinationChangedListener { _, destination, _ ->
        when (destination.route) {
            NavDestination.LIST_MOVIE.route -> 0
            NavDestination.LIST_TV.route -> 1
            NavDestination.SETTINGS.route -> 2
            else -> null
        }?.let { newIndex ->
            selectedTabIndex = newIndex
        }
    }
    NavigationBar(
        containerColor = cardColor,
        modifier = Modifier.graphicsLayer {
            shape = RectangleShape
            shadowElevation = 20f
        }
    ) {
        BottomNavigationItem(
            scope = this,
            isSelected = selectedTabIndex == 0,
            title = stringResource(id = R.string.tab_title_movieList),
            icon = Icons.Filled.Movie
        ) {
            navController.navigate(NavDestination.LIST_MOVIE.route) {
                launchSingleTop = true
            }
        }
        BottomNavigationItem(
            scope = this,
            isSelected = selectedTabIndex == 1,
            title = stringResource(id = R.string.tab_title_tvList),
            icon = Icons.Filled.Tv
        ) {
            navController.navigate(NavDestination.LIST_TV.route) {
                launchSingleTop = true
            }
        }
        BottomNavigationItem(
            scope = this,
            isSelected = selectedTabIndex == 2,
            title = stringResource(id = R.string.tab_title_settings),
            icon = Icons.Filled.Settings
        ) {
            navController.navigate(NavDestination.SETTINGS.route) {
                launchSingleTop = true
            }
        }
    }
    if (selectedTabIndex != 0) {
        BackHandler {
            navController.navigate(LIST_MOVIE.route) {
                popUpTo(0)
            }
        }
    }
}

@Composable
fun BottomNavigationItem(
    scope: RowScope,
    isSelected: Boolean,
    title: String,
    icon: ImageVector,
    onSelected: () -> Unit,
) {
    with(scope) {
        NavigationBarItem(
            selected = isSelected,
            onClick = onSelected,
            icon = {
                Icon(
                    imageVector = icon,
                    tint = if (isSelected) lightNavItemColor else darkNavItemColor,
                    contentDescription = null,
                )
            },
            label = {
                Text(
                    text = title,
                    style = if (isSelected) navigationBarItemTitleSelected else navigationBarItemTitleUnselected,
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = cardColor,
            ),
        )
    }
}