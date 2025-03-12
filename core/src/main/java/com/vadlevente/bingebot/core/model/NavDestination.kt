package com.vadlevente.bingebot.core.model

import kotlinx.serialization.Serializable

sealed interface NavDestination {

    sealed interface TopNavDestination : NavDestination {
        @Serializable
        data object Splash : TopNavDestination
        @Serializable
        data object NonAuthenticatedScreens : TopNavDestination
        @Serializable
        data object AuthenticatedScreens : TopNavDestination
        @Serializable
        data object Authenticate : TopNavDestination
    }

    sealed interface NonAuthenticatedNavDestination : NavDestination {

        @Serializable
        data object Registration : NonAuthenticatedNavDestination

        @Serializable
        data object Login : NonAuthenticatedNavDestination

        @Serializable
        data class RegisterPin(
            val email: String,
            val password: String,
        ) : NonAuthenticatedNavDestination

        @Serializable
        data object RegisterPinConfirm : NonAuthenticatedNavDestination

        @Serializable
        data class BiometricsRegistration(
            val email: String,
            val password: String,
        ) : NonAuthenticatedNavDestination
    }

    sealed interface AuthenticatedNavDestination : NavDestination {
        @Serializable
        data object Dashboard : AuthenticatedNavDestination
        @Serializable
        data object ListMovie : AuthenticatedNavDestination
        @Serializable
        data object ListTv : AuthenticatedNavDestination
        @Serializable
        data object SearchMovie : AuthenticatedNavDestination
        @Serializable
        data object SearchTv : AuthenticatedNavDestination
        @Serializable
        data class MovieDetails(
            val movieId: Int,
        ) : AuthenticatedNavDestination

        @Serializable
        data class TvDetails(
            val tvId: Int,
        ) : AuthenticatedNavDestination

        @Serializable
        data class MovieWatchList(
            val watchListId: String,
        ) : AuthenticatedNavDestination

        @Serializable
        data class TvWatchList(
            val watchListId: String,
        ) : AuthenticatedNavDestination

        @Serializable
        data object Settings : AuthenticatedNavDestination
    }

}