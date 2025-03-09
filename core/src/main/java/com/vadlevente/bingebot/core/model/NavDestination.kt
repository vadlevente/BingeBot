package com.vadlevente.bingebot.core.model

import kotlinx.serialization.Serializable

sealed interface NavDestination {

    @Serializable data object Splash : NavDestination
    @Serializable data object Registration : NavDestination
    @Serializable data object Login : NavDestination
    @Serializable data class RegisterPin(
        val email: String,
        val password: String,
    ) : NavDestination
    @Serializable data object RegisterPinConfirm : NavDestination
    @Serializable data object Authenticate : NavDestination
    @Serializable data class BiometricsRegistration(
        val email: String,
        val password: String,
    ) : NavDestination
    @Serializable data object Dashboard : NavDestination
    @Serializable data object ListMovie : NavDestination
    @Serializable data object ListTv : NavDestination
    @Serializable data object SearchMovie : NavDestination
    @Serializable data object SearchTv : NavDestination
    @Serializable data class MovieDetails(
        val movieId: Int,
    ) : NavDestination
    @Serializable data class TvDetails(
        val tvId: Int,
    ) : NavDestination
    @Serializable data class MovieWatchList(
        val watchListId: String,
    ) : NavDestination
    @Serializable data class TvWatchList(
        val watchListId: String,
    ) : NavDestination
    @Serializable data object Settings : NavDestination
}