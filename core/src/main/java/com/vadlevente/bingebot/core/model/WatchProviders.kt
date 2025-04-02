package com.vadlevente.bingebot.core.model

import com.google.gson.annotations.SerializedName

data class WatchProviders(
    val flatrate: List<ProviderInfo>?,
    val buy: List<ProviderInfo>?,
    val rent: List<ProviderInfo>?,
)

data class ProviderInfo(
    @SerializedName("logo_path")
    val path: String,
    @SerializedName("provider_name")
    val name: String,
)
