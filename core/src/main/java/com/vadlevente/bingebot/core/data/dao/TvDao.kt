package com.vadlevente.bingebot.core.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vadlevente.bingebot.core.model.Item.Tv
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface TvDao : ItemDao<Tv> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertItem(item: Tv)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertItems(items: List<Tv>)

    @Delete
    override suspend fun deleteItem(item: Tv)

    @Query("DELETE from tv WHERE id = :itemId")
    override suspend fun deleteItem(itemId: Int)

    @Query("SELECT * from tv")
    override fun getAllItems(): Flow<List<Tv>>

    @Query("SELECT * from tv WHERE id in (:itemIds)")
    override fun getItems(itemIds: List<Int>): Flow<List<Tv>>

    @Query("SELECT * from tv WHERE localeCode != :localeCode")
    override fun getAllIncorrectlyLocalizedItems(localeCode: String): Flow<List<Tv>>

    @Query("UPDATE tv SET watchedDate = :watchedDate WHERE id = :itemId")
    override suspend fun setItemWatchedDate(itemId: Int, watchedDate: Date?)

}