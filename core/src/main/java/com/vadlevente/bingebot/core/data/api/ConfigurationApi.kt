package com.vadlevente.bingebot.core.data.api

import com.vadlevente.bingebot.core.model.ApiConfiguration
import retrofit2.http.GET

interface ConfigurationApi {

    @GET("configuration")
    suspend fun fetchConfiguration(): ApiConfiguration

}