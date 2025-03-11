package com.vadlevente.bingebot.core.data.dao

import com.vadlevente.bingebot.core.model.Item
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface ItemDao<T : Item> {

    suspend fun insertItem(item: T)

    suspend fun insertItems(items: List<@JvmSuppressWildcards T>)

    suspend fun deleteItem(item: T)

    suspend fun deleteItem(itemId: Int)

    fun getAllItems(): Flow<List<T>>

    fun getItems(itemIds: List<Int>): Flow<List<T>>

    fun getItem(itemId: Int): Flow<T?>

    fun getAllIncorrectlyLocalizedItems(localeCode: String): Flow<List<T>>

    suspend fun setItemWatchedDate(itemId: Int, watchedDate: Date?)

}