package com.vadlevente.bingebot.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vadlevente.bingebot.core.model.WatchList
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchList(watchList: WatchList)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchLists(watchLists: List<WatchList>)

    @Query("DELETE from watchList WHERE watchListId = :watchListId")
    fun deleteWatchList(watchListId: String)

    @Query("DELETE from watchList")
    fun deleteAll()

    @Query("SELECT * from watchList")
    fun getAllWatchLists(): Flow<List<WatchList>>

    @Query("SELECT * from watchList WHERE watchListId = :watchListId")
    fun getWatchList(watchListId: String): Flow<WatchList>

}