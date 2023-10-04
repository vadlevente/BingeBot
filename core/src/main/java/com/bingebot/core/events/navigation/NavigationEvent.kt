package com.bingebot.core.events.navigation

sealed interface NavigationEvent {

    data class NavigateTo(
        val routeBase: String,
        val arguments: List<Any> = emptyList(),
    ) : NavigationEvent {
        val route: String
            get() = buildString {
                append(routeBase)
                arguments.forEach {
                    append("/$it")
                }
            }
    }

    object NavigateUp : NavigationEvent

}
