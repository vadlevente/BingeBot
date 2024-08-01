package com.vadlevente.bingebot.core.data.repository

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.data.local.db.MovieDatabase
import com.vadlevente.bingebot.core.data.remote.ConfigurationRemoteDataSource
import com.vadlevente.bingebot.core.data.service.AuthenticationService
import com.vadlevente.bingebot.core.model.Item.Movie
import com.vadlevente.bingebot.core.model.Item.Tv
import com.vadlevente.bingebot.core.model.SelectedLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ConfigurationRepository {
    suspend fun updateConfiguration()
    suspend fun logout()
    suspend fun setSelectedLanguage(language: SelectedLanguage)

}

class ConfigurationRepositoryImpl @Inject constructor(
    private val configurationRemoteDataSource: ConfigurationRemoteDataSource,
    private val preferencesDataSource: PreferencesDataSource,
    private val authenticationService: AuthenticationService,
    private val movieRepository: ItemRepository<Movie>,
    private val tvRepository: ItemRepository<Tv>,
    private val db: MovieDatabase,
) : ConfigurationRepository {

    override suspend fun updateConfiguration() = withContext(Dispatchers.IO) {
        val config = configurationRemoteDataSource.getConfiguration()
        preferencesDataSource.saveApiConfiguration(config)
        setLocale(preferencesDataSource.language.first().code)
    }

    override suspend fun logout() = withContext(Dispatchers.IO) {
        authenticationService.logout()
        db.clearAllTables()
        preferencesDataSource.saveActiveProfileId(null)
    }

    override suspend fun setSelectedLanguage(language: SelectedLanguage) =
        withContext(Dispatchers.IO) {
            preferencesDataSource.saveSelectedLanguage(language)
            movieRepository.updateItemLocalizations()
            movieRepository.updateGenres()
            tvRepository.updateItemLocalizations()
            tvRepository.updateGenres()
            setLocale(language.code)
        }

    private fun setLocale(code: String) {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(code)
        )
    }

}