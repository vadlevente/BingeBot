package com.vadlevente.moviedetails.domain.model

data class DisplayedWatchProviders(
    val flatrate: List<DisplayedProviderInfo>,
    val buy: List<DisplayedProviderInfo>,
    val rent: List<DisplayedProviderInfo>,
) {
    val hasData: Boolean
        get() = !(flatrate.isEmpty() && buy.isEmpty() && rent.isEmpty())
}

data class DisplayedProviderInfo(
    val fullPath: String,
    val name: String,
)

