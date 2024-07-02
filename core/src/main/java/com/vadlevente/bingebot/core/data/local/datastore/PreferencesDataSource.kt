package com.vadlevente.bingebot.core.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
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
) {

    companion object {
        private const val ACTIVE_PROFILE_ID = "activeProfileId"
    }

    private val data = dataStore.data.catch {
        throw BingeBotException(DATA_READ_ERROR)
    }.flowOn(Dispatchers.IO)

    val activeProfileId: Flow<String?> = data.map {
        it[stringPreferencesKey(ACTIVE_PROFILE_ID)]
    }

    suspend fun saveActiveProfileId(value: String?) {
        value?.let {
            savePreference(stringPreferencesKey(ACTIVE_PROFILE_ID), value)
        } ?: removePreference(stringPreferencesKey(ACTIVE_PROFILE_ID))
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