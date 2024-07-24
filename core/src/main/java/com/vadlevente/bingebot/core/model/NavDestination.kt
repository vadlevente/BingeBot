package com.vadlevente.bingebot.core.model

enum class NavDestination(val route: String) {
    SPLASH("splash"),
    REGISTRATION("registration"),
    LOGIN("login"),
    DASHBOARD("dashboard"),
    LIST_MOVIE("listMovie"),
    LIST_TV("listTv"),
    SETTINGS("settings"),
    SEARCH_MOVIE("searchMovie"),
    MOVIE_DETAILS("movieDetails"),
    WATCH_LIST("watchList"),
}