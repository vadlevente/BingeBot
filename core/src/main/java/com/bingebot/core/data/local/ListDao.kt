package com.bingebot.core.data.local

import com.bingebot.core.model.ModelWithId
import kotlinx.coroutines.flow.Flow

interface ListDao<TKey, TModel: ModelWithId<TKey>> {
    fun observeById(id: TKey): Flow<TModel>
    fun observeAll(): Flow<List<TModel>>
    suspend fun getAll(): List<TModel>
    suspend fun getById(id: TKey): TModel?
    suspend fun save(item: TModel)
    suspend fun saveAll(items: List<TModel>)
    suspend fun deleteById(id: TKey)
    suspend fun deleteAll()
}