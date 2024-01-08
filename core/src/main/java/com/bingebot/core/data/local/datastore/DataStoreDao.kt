package com.bingebot.core.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bingebot.core.model.Model
import com.bingebot.core.model.ModelWithId
import com.bingebot.core.util.getTypeAsClass
import com.bingebot.core.util.toJson
import com.bingebot.core.util.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.bingebot.core.data.local.ItemDao as BaseItemDao
import com.bingebot.core.data.local.ListDao as BaseListDao

sealed class DataStoreDao @Inject constructor() {

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    protected suspend fun getPreference(key: Preferences.Key<String>) = dataStore.data
        .firstOrNull()
        ?.get(key).takeIf { it != null }

    @Suppress("UNCHECKED_CAST")
    protected suspend fun getAllPreference(keyPrefix: String): List<String> = dataStore.data
        .firstOrNull()
        ?.filterListPreferences(keyPrefix)?.values?.toList() as? List<String> ?: emptyList()

    protected fun observePreference(key: Preferences.Key<String>): Flow<String> = dataStore.data
        .filter { it.contains(key) }
        .map { it[key]!! }
        .distinctUntilChanged()

    @Suppress("UNCHECKED_CAST")
    protected fun observeAllPreference(keyPrefix: String): Flow<List<String>> = dataStore.data
        .map {
            it.filterListPreferences(keyPrefix).values.toList() as List<String>
        }
        .distinctUntilChanged()

    protected suspend fun savePreference(
        preferencesKey: Preferences.Key<String>,
        value: String,
    ) = updateDataAsync { this[preferencesKey] = value }

    protected suspend fun removePreference(preferencesKey: Preferences.Key<String>) =
        updateDataAsync { remove(preferencesKey) }

    protected suspend fun removeAllPreference(keyPrefix: String) =
        updateDataAsync {
            filterListPreferences(keyPrefix).forEach {
                remove(it.key)
            }
        }

    private suspend fun updateDataAsync(action: MutablePreferences.() -> Unit) {
        dataStore.updateData { preferences ->
            preferences.toMutablePreferences().apply {
                action()
            }
        }
    }

    private fun Preferences.filterListPreferences(keyPrefix: String) =
        asMap().filterKeys { it.name.startsWith("$keyPrefix::") }

    abstract class ItemDao<TItem : Model>(
        key: String,
    ) : DataStoreDao(), BaseItemDao<TItem> {

        private val preferenceKey = stringPreferencesKey(key)

        private fun toObject(serialized: String?): TItem? =
            serialized.toObject(getTypeAsClass(this))

        override fun observe(): Flow<TItem> =
            observePreference(preferenceKey).map { item ->
                toObject(item) ?: throw NullPointerException(
                    "Null in ItemDao observe. Use observeAsOptional method!"
                )
            }

        override suspend fun get(): TItem? =
            getPreference(preferenceKey)?.let { toObject(it) }

        override suspend fun save(item: TItem) =
            savePreference(preferenceKey, item.toJson())

        override suspend fun delete() =
            removePreference(preferenceKey)
    }

    abstract class ListDao<TKey, TItem : ModelWithId<TKey>>(
        private val keyPrefix: String,
    ) : DataStoreDao(), BaseListDao<TKey, TItem> {

        private fun toObject(serialized: String?): TItem? =
            serialized.toObject(getTypeAsClass(this, 1))

        private fun toObjects(serializedList: List<String>): List<TItem> =
            serializedList.map { toObject(it)!! }

        private fun getKey(id: TKey) = stringPreferencesKey("$keyPrefix::$id")

        override fun observeById(id: TKey): Flow<TItem> =
            observePreference(getKey(id)).map { toObject(it)!! }

        override fun observeAll(): Flow<List<TItem>> =
            observeAllPreference(keyPrefix).map { toObjects(it) }

        override suspend fun getAll(): List<TItem> =
            toObjects(getAllPreference(keyPrefix))

        override suspend fun getById(id: TKey): TItem? =
            getPreference(getKey(id))?.let { toObject(it) }

        override suspend fun save(item: TItem) =
            savePreference(getKey(item.id), item.toJson())

        override suspend fun saveAll(items: List<TItem>) =
            items.forEach {
                save(it)
            }

        override suspend fun deleteById(id: TKey) =
            removePreference(getKey(id))

        override suspend fun deleteAll() =
            removeAllPreference(keyPrefix)
    }

}