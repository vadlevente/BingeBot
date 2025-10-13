package com.vadlevente.bingebot.app.ui.authenticated

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.vadlevente.bingebot.core.events.navigation.NavigationEvent.AuthenticatedNavigationEvent
import com.vadlevente.bingebot.core.events.navigation.NavigationEventChannel
import com.vadlevente.bingebot.core.model.NavDestination.AuthenticatedNavDestination
import com.vadlevente.bingebot.core.ui.composables.horizontalSlideComposable
import com.vadlevente.bingebot.core.util.SetupLifecycle
import com.vadlevente.bingebot.dashboard.ui.DashboardScreen
import com.vadlevente.bingebot.search.ui.SearchMovieScreen
import com.vadlevente.bingebot.search.ui.SearchTvScreen
import com.vadlevente.bingebot.watchlist.ui.movie.MovieWatchListScreen
import com.vadlevente.bingebot.watchlist.ui.tv.TvWatchListScreen
import com.vadlevente.moviedetails.ui.MovieDetailsScreen
import com.vadlevente.moviedetails.ui.PersonDetailsScreen
import com.vadlevente.moviedetails.ui.TvDetailsScreen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

@Composable
fun AuthenticatedScreens(
    navigationEventChannel: NavigationEventChannel,
    navController: NavHostController = rememberNavController(),
    viewModel: AuthenticatedScreensViewModel = hiltViewModel()
) {
    SetupLifecycle(viewModel)
    val state by viewModel.state.collectAsStateWithLifecycle()
    if (!state.shouldShowScreens) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        )
        return
    }
    CollectEvents(
        navigationEventChannel,
        navController,
    )
    NavHost(
        navController = navController,
        startDestination = AuthenticatedNavDestination.Dashboard
    ) {
        composable<AuthenticatedNavDestination.Dashboard> {
            DashboardScreen()
        }
        horizontalSlideComposable<AuthenticatedNavDestination.SearchMovie> {
            SearchMovieScreen()
        }
        horizontalSlideComposable<AuthenticatedNavDestination.SearchTv> {
            SearchTvScreen()
        }
        horizontalSlideComposable<AuthenticatedNavDestination.MovieDetails> {
            val args: AuthenticatedNavDestination.MovieDetails = it.toRoute()
            MovieDetailsScreen(args.movieId)
        }
        horizontalSlideComposable<AuthenticatedNavDestination.TvDetails> {
            val args: AuthenticatedNavDestination.TvDetails = it.toRoute()
            TvDetailsScreen(args.tvId)
        }
        horizontalSlideComposable<AuthenticatedNavDestination.MovieWatchList> {
            val args: AuthenticatedNavDestination.MovieWatchList = it.toRoute()
            MovieWatchListScreen(args.watchListId)
        }
        horizontalSlideComposable<AuthenticatedNavDestination.TvWatchList> {
            val args: AuthenticatedNavDestination.TvWatchList = it.toRoute()
            TvWatchListScreen(args.watchListId)
        }
        horizontalSlideComposable<AuthenticatedNavDestination.PersonDetails> {
            val args: AuthenticatedNavDestination.PersonDetails = it.toRoute()
            PersonDetailsScreen(args.personId)
        }
    }
    val intent = (LocalContext.current as? AppCompatActivity)?.intent
    LaunchedEffect(intent) {
        intent?.dataString?.let { data ->
            when (data) {
                SHORTCUT_SEARCH_MOVIE -> AuthenticatedNavDestination.SearchMovie
                SHORTCUT_SEARCH_TV -> AuthenticatedNavDestination.SearchTv
                else -> null
            }?.let { navController.navigate(it) }
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
            navigationEventChannel.events.filterIsInstance<AuthenticatedNavigationEvent>().collectLatest { event ->
                when (event) {
                    is AuthenticatedNavigationEvent.NavigateTo -> navController.navigate(event.destination)
                    AuthenticatedNavigationEvent.NavigateUp -> navController.popBackStack()
                    else -> {}
                }
            }
        }
    }
}

private const val SHORTCUT_SEARCH_MOVIE = "searchMovie"
private const val SHORTCUT_SEARCH_TV = "searchTv"
