package com.vadlevente.bingebot.core.events.navigation

import com.vadlevente.bingebot.core.model.NavDestination

sealed interface NavigationEvent {

    data class NavigateTo(
        val destination: NavDestination,
    ) : NavigationEvent
    object NavigateUp : NavigationEvent
    object ExitApplication : NavigationEvent

}
