package com.vadlevente.bingebot.core.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.vadlevente.bingebot.core.model.ApiConfiguration
import com.vadlevente.bingebot.core.model.SelectedLanguage
import com.vadlevente.bingebot.core.model.exception.BingeBotException
import com.vadlevente.bingebot.core.model.exception.Reason.DATA_READ_ERROR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val gson: Gson,
) {

    companion object {
        private const val ACTIVE_PROFILE_ID = "activeProfileId"
        private const val API_CONFIGURATION = "apiConfiguration"
        private const val LANGUAGE = "language"
        private const val PIN_ENCRYPTED_SECRET = "pinEncryptedSecret"
        private const val BIOMETRICS_ENCRYPTED_SECRET = "biometricsEncryptedSecret"
        private const val DISPLAY_NEXT_TO_WATCH = "displayNextToWatch"
    }

    private val data = dataStore.data.catch {
        throw BingeBotException(it, DATA_READ_ERROR)
    }.flowOn(Dispatchers.IO)

    val activeProfileId: Flow<String?> = data.map {
        it[stringPreferencesKey(ACTIVE_PROFILE_ID)]
    }

    val apiConfiguration: Flow<ApiConfiguration> = data.map {
        gson.fromJson(it[stringPreferencesKey(API_CONFIGURATION)], ApiConfiguration::class.java)
    }

    val language: Flow<SelectedLanguage?> = data.map {
        it[stringPreferencesKey(LANGUAGE)]?.let { code ->
            SelectedLanguage.from(code)
        }
    }

    val pinEncryptedSecret: Flow<String?> = data.map {
        it[stringPreferencesKey(PIN_ENCRYPTED_SECRET)]
    }

    val biometricsEncryptedSecret: Flow<String?> = data.map {
        it[stringPreferencesKey(BIOMETRICS_ENCRYPTED_SECRET)]
    }

    val displayNextToWatch: Flow<Boolean> = data.map {
        it[booleanPreferencesKey(DISPLAY_NEXT_TO_WATCH)] != false
    }

    suspend fun saveActiveProfileId(value: String?) {
        value?.let {
            savePreference(stringPreferencesKey(ACTIVE_PROFILE_ID), value)
        } ?: removePreference(stringPreferencesKey(ACTIVE_PROFILE_ID))
    }

    suspend fun saveApiConfiguration(value: ApiConfiguration) {
        savePreference(stringPreferencesKey(API_CONFIGURATION), gson.toJson(value))
    }

    suspend fun saveSelectedLanguage(value: SelectedLanguage) {
        savePreference(stringPreferencesKey(LANGUAGE), value.code)
    }

    suspend fun savePinEncryptedSecret(value: String) {
        savePreference(stringPreferencesKey(PIN_ENCRYPTED_SECRET), value)
    }

    suspend fun saveBiometricsEncryptedSecret(value: String) {
        savePreference(stringPreferencesKey(BIOMETRICS_ENCRYPTED_SECRET), value)
    }

    suspend fun saveDisplayNextToWatch(value: Boolean) {
        savePreference(booleanPreferencesKey(DISPLAY_NEXT_TO_WATCH), value)
    }

    suspend fun clearUserRelatedData() {
        removePreference(stringPreferencesKey(ACTIVE_PROFILE_ID))
        removePreference(stringPreferencesKey(PIN_ENCRYPTED_SECRET))
        removePreference(stringPreferencesKey(BIOMETRICS_ENCRYPTED_SECRET))
    }

    suspend fun clearSecret() {
        removePreference(stringPreferencesKey(PIN_ENCRYPTED_SECRET))
        removePreference(stringPreferencesKey(BIOMETRICS_ENCRYPTED_SECRET))
    }

    private suspend fun <T> savePreference(
        preferencesKey: Preferences.Key<T>,
        value: T,
    ) = updateDataAsync { this[preferencesKey] = value }

    private suspend fun removePreference(preferencesKey: Preferences.Key<*>) =
        updateDataAsync { remove(preferencesKey) }

    suspend fun removeAllPreferences(except: Set<String> = emptySet()) =
        updateDataAsync {
            listOf(
                ACTIVE_PROFILE_ID
            ).minus(except)
                .forEach {
                    remove(stringPreferencesKey(it))
                }
        }

    private suspend fun updateDataAsync(action: MutablePreferences.() -> Unit) {
        dataStore.edit { preferences ->
            preferences.apply {
                action()
            }
        }
    }

}