package com.vadlevente.bingebot.core.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vadlevente.bingebot.core.model.Item.Movie
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface MovieDao : ItemDao<Movie> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertItem(item: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertItems(items: List<Movie>)

    @Delete
    override suspend fun deleteItem(item: Movie)

    @Query("DELETE from movie WHERE id = :itemId")
    override suspend fun deleteItem(itemId: Int)

    @Query("SELECT * from movie")
    override fun getAllItems(): Flow<List<Movie>>

    @Query("SELECT * from movie WHERE id in (:itemIds)")
    override fun getItems(itemIds: List<Int>): Flow<List<Movie>>

    @Query("SELECT * from movie WHERE id = :itemId ")
    override fun getItem(itemId: Int): Flow<Movie?>

    @Query("SELECT * from movie WHERE localeCode != :localeCode")
    override fun getAllIncorrectlyLocalizedItems(localeCode: String): Flow<List<Movie>>

    @Query("UPDATE movie SET watchedDate = :watchedDate WHERE id = :itemId")
    override suspend fun setItemWatchedDate(itemId: Int, watchedDate: Date?)

}