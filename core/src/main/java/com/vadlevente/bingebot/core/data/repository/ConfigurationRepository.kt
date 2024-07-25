package com.vadlevente.bingebot.core.data.repository

import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.data.remote.ConfigurationRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ConfigurationRepository {
    suspend fun updateConfiguration()

}

class ConfigurationRepositoryImpl @Inject constructor(
    private val configurationRemoteDataSource: ConfigurationRemoteDataSource,
    private val preferencesDataSource: PreferencesDataSource,
) : ConfigurationRepository {

    override suspend fun updateConfiguration() = withContext(Dispatchers.IO) {
        val config = configurationRemoteDataSource.getConfiguration()
        preferencesDataSource.saveApiConfiguration(config)
    }

}