package com.vadlevente.bingebot.dashboard.ui

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
import androidx.compose.runtime.mutableStateOf
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
import com.vadlevente.bingebot.dashboard.R
import com.vadlevente.bingebot.list.ui.MovieListScreen
import com.vadlevente.bingebot.list.ui.TvListScreen
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
                Text(text = "BEÁLLÍTÁSOK!!")
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

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
            selectedTabIndex = 0
            navController.navigate(NavDestination.LIST_MOVIE.route)
        }
        BottomNavigationItem(
            scope = this,
            isSelected = selectedTabIndex == 1,
            title = stringResource(id = R.string.tab_title_tvList),
            icon = Icons.Filled.Tv
        ) {
            selectedTabIndex = 1
            navController.navigate(NavDestination.LIST_TV.route)
        }
        BottomNavigationItem(
            scope = this,
            isSelected = selectedTabIndex == 2,
            title = stringResource(id = R.string.tab_title_settings),
            icon = Icons.Filled.Settings
        ) {
            selectedTabIndex = 2
            navController.navigate(NavDestination.SETTINGS.route)
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