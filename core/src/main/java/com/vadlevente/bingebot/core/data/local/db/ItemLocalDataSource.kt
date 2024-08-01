package com.vadlevente.bingebot.core.data.local.db

import com.vadlevente.bingebot.core.data.dao.ItemDao
import com.vadlevente.bingebot.core.data.dao.WatchListDao
import com.vadlevente.bingebot.core.model.Genre
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.model.WatchList
import kotlinx.coroutines.flow.Flow
import java.util.Date

abstract class ItemLocalDataSource <T : Item> (
    private val itemDao: ItemDao<T>,
    private val watchListDao: WatchListDao,
) {

    fun getAllItems() = itemDao.getAllItems()
    fun getItems(itemIds: List<Int>) = itemDao.getItems(itemIds)

    fun getAllItemsWithIncorrectLocalization(locale: String) = itemDao.getAllIncorrectlyLocalizedItems(locale)

    fun getWatchList(watchListId: String) = watchListDao.getWatchList(watchListId)

    suspend fun updateItem(item: T) {
        itemDao.insertItem(item)
    }

    suspend fun updateItems(items: List<T>) {
        itemDao.insertItems(items)
    }

    suspend fun insertItem(item: T) {
        itemDao.insertItem(item)
    }

    suspend fun deleteItem(item: T) {
        itemDao.deleteItem(item)
    }

    suspend fun deleteItem(itemId: Int) {
        itemDao.deleteItem(itemId)
    }

    suspend fun deleteWatchList(watchListId: String) {
        watchListDao.delete(watchListId)
    }

    suspend fun setItemWatchedDate(itemId: Int, watchedDate: Date?) {
        itemDao.setItemWatchedDate(itemId, watchedDate)
    }

    abstract fun getAllWatchLists(): Flow<List<WatchList>>
    abstract suspend fun deleteAllWatchLists()
    abstract suspend fun insertWatchList(watchList: WatchList)
    abstract suspend fun insertWatchLists(watchLists: List<WatchList>)
    abstract fun getAllGenres(): Flow<List<Genre>>
    abstract suspend fun insertGenres(genres: List<Genre>)
    abstract suspend fun deleteAllGenres()


}