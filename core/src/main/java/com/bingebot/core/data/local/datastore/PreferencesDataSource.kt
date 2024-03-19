package com.bingebot.core.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty
import javax.inject.Inject

class PreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {

    companion object {
        val ACTIVE_PROFILE_ID = stringPreferencesKey("activeProfileId")
    }

    val activeProfileId: Flow<String>
        get() = observePreference(ACTIVE_PROFILE_ID)

    suspend fun saveActiveProfileId(value: String) {
        savePreference(ACTIVE_PROFILE_ID, value)
    }

    private fun <T> observePreference(key: Preferences.Key<T>): Flow<T> = dataStore.data
        .onEmpty {
            var a = 1
        }
        .filter { it.contains(key) }
        .onEmpty {
            var a = 1
        }
        .map { it[key]!! }
        .distinctUntilChanged()

    private suspend fun <T> savePreference(
        preferencesKey: Preferences.Key<T>,
        value: T,
    ) = updateDataAsync { this[preferencesKey] = value }

    private suspend fun <T> removePreference(preferencesKey: Preferences.Key<T>) =
        updateDataAsync { remove(preferencesKey) }

    suspend fun removeAllPreferences(except: Preferences.Key<*>) =
        updateDataAsync {
            listOf(
                ACTIVE_PROFILE_ID
            ).minus(except)
                .forEach {
                    remove(it)
                }
        }

    private suspend fun updateDataAsync(action: MutablePreferences.() -> Unit) {
        dataStore.updateData { preferences ->
            preferences.toMutablePreferences().apply {
                action()
            }
        }
    }

}