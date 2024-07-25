package com.vadlevente.bingebot.core.data.remote

import com.vadlevente.bingebot.core.data.api.ConfigurationApi
import javax.inject.Inject

class ConfigurationRemoteDataSource @Inject constructor(
    private val configurationApi: ConfigurationApi,
) {

    suspend fun getConfiguration() = configurationApi.fetchConfiguration()

}