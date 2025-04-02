package com.vadlevente.moviedetails.domain.model

data class DisplayedWatchProviders(
    val flatrate: List<DisplayedProviderInfo>?,
    val buy: List<DisplayedProviderInfo>?,
    val rent: List<DisplayedProviderInfo>?,
) {
    val hasData: Boolean
        get() = !(flatrate.isNullOrEmpty() && buy.isNullOrEmpty() && rent.isNullOrEmpty())
}

data class DisplayedProviderInfo(
    val fullPath: String,
    val name: String,
)

