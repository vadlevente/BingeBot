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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
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
import com.vadlevente.bingebot.dashboard.R
import com.vadlevente.bingebot.list.ui.MovieListScreen
import com.vadlevente.bingebot.list.ui.TvListScreen
import com.vadlevente.bingebot.settings.SettingsScreen
import com.vadlevente.bingebot.ui.BingeBotTheme

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
            startDestination = NavDestination.ListMovie
        ) {
            composable<NavDestination.ListMovie> {
                MovieListScreen()
            }
            composable<NavDestination.ListTv> {
                TvListScreen()
            }
            composable<NavDestination.Settings> {
                SettingsScreen()
            }
        }
    }

}

@Composable
fun BottomNavigationBar(navController: NavController) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
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
            navController.navigate(NavDestination.ListMovie) {
                launchSingleTop = true
            }
            selectedTabIndex = 0
        }
        BottomNavigationItem(
            scope = this,
            isSelected = selectedTabIndex == 1,
            title = stringResource(id = R.string.tab_title_tvList),
            icon = Icons.Filled.Tv
        ) {
            navController.navigate(NavDestination.ListTv) {
                launchSingleTop = true
            }
            selectedTabIndex = 1
        }
        BottomNavigationItem(
            scope = this,
            isSelected = selectedTabIndex == 2,
            title = stringResource(id = R.string.tab_title_settings),
            icon = Icons.Filled.Settings
        ) {
            navController.navigate(NavDestination.Settings) {
                launchSingleTop = true
            }
            selectedTabIndex = 2
        }
    }
    if (selectedTabIndex != 0) {
        BackHandler {
            navController.navigate(NavDestination.ListMovie) {
                popUpTo(0)
            }
            selectedTabIndex = 0
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
                    tint = if (isSelected) BingeBotTheme.colors.highlight else MaterialTheme.colorScheme.primary,
                    contentDescription = null,
                )
            },
            label = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSelected) BingeBotTheme.colors.highlight else MaterialTheme.colorScheme.primary
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent,
            ),
        )
    }
}