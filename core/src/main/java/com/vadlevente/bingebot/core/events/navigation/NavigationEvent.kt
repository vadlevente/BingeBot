package com.vadlevente.bingebot.core.events.navigation

import com.vadlevente.bingebot.core.model.NavDestination.AuthenticatedNavDestination
import com.vadlevente.bingebot.core.model.NavDestination.NonAuthenticatedNavDestination
import com.vadlevente.bingebot.core.model.NavDestination.TopNavDestination

sealed interface NavigationEvent {

    sealed interface TopNavigationEvent : NavigationEvent {
        data class NavigateTo(
            val destination: TopNavDestination
        ) : TopNavigationEvent
        object NavigateUp : TopNavigationEvent
        object ExitApplication : TopNavigationEvent
    }

    sealed interface AuthenticatedNavigationEvent : NavigationEvent {
        data class NavigateTo(
            val destination: AuthenticatedNavDestination,
        ) : AuthenticatedNavigationEvent

        object NavigateUp : AuthenticatedNavigationEvent
    }

    sealed interface NonAuthenticatedNavigationEvent : NavigationEvent {
        data class NavigateTo(
            val destination: NonAuthenticatedNavDestination,
        ) : NonAuthenticatedNavigationEvent

        object NavigateUp : NonAuthenticatedNavigationEvent
    }

}
