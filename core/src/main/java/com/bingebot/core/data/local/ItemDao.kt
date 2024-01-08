package com.bingebot.core.data.local

import com.bingebot.core.model.Model
import kotlinx.coroutines.flow.Flow

interface ItemDao<T : Model> {
    fun observe(): Flow<T>
    suspend fun get(): T?
    suspend fun save(item: T)
    suspend fun delete()
}