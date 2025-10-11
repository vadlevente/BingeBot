package com.vadlevente.bingebot.core.model

import kotlinx.serialization.Serializable

sealed interface NavDestination {

    sealed interface TopNavDestination : NavDestination {
        @Serializable
        data object Splash : TopNavDestination
        @Serializable
        data object Onboarding : TopNavDestination
        @Serializable
        data object AuthenticatedScreens : TopNavDestination
        @Serializable
        data object Authenticate : TopNavDestination
        @Serializable
        data class EnrollSecurity(
            val email: String? = null,
            val password: String? = null,
            val canStepBack: Boolean,
        ) : TopNavDestination
    }

    sealed interface OnboardingNavDestination : NavDestination {

        @Serializable
        data object Registration : OnboardingNavDestination

        @Serializable
        data object Login : OnboardingNavDestination
    }

    sealed interface EnrollSecurityNavDestination : NavDestination {

        @Serializable
        data object RegisterPin : EnrollSecurityNavDestination

        @Serializable
        data object RegisterPinConfirm : EnrollSecurityNavDestination

        @Serializable
        data class BiometricsRegistration(
            val email: String,
            val password: String,
        ) : EnrollSecurityNavDestination
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

        @Serializable
        data class PersonDetails(
            val personId: Int,
        ) : AuthenticatedNavDestination
    }

}