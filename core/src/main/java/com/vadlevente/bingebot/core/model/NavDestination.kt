package com.vadlevente.bingebot.core.model

enum class NavDestination(val route: String) {
    SPLASH("splash"),
    REGISTRATION("registration"),
    LOGIN("login"),
    LIST_MOVIE("listMovie"),
    SEARCH_MOVIE("searchMovie"),
    MOVIE_DETAILS("movieDetails"),
}