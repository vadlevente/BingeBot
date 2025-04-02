package com.vadlevente.bingebot.core.model.dto

import com.google.gson.annotations.SerializedName
import com.vadlevente.bingebot.core.model.WatchProviders

data class ProvidersDto(
    val results: ProviderDto
)

data class ProviderDto(
    @SerializedName("HU")
    val hu: WatchProviders?
)
