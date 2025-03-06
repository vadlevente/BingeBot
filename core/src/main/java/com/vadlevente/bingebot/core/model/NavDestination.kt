package com.vadlevente.bingebot.core.model

enum class NavDestination(val route: String) {
    SPLASH("splash"),
    REGISTRATION("registration"),
    LOGIN("login"),
    REGISTER_PIN("registerPin"),
    REGISTER_PIN_CONFIRM("registerPinConfirm"),
    AUTHENTICATE("authenticate"),
    BIOMETRICS_REGISTRATION("biometricRegistration"),
    DASHBOARD("dashboard"),
    LIST_MOVIE("listMovie"),
    LIST_TV("listTv"),
    SETTINGS("settings"),
    SEARCH_MOVIE("searchMovie"),
    MOVIE_DETAILS("movieDetails"),
    MOVIE_WATCH_LIST("movieWatchList"),
    SEARCH_TV("searchTv"),
    TV_DETAILS("tvDetails"),
    TV_WATCH_LIST("tvWatchList"),
}