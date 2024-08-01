package com.vadlevente.bingebot.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vadlevente.bingebot.core.model.Genre
import com.vadlevente.bingebot.core.model.ItemType
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao {

    @Query("SELECT * from genre WHERE type = :type")
    fun getAllGenres(type: ItemType): Flow<List<Genre>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<Genre>)

    @Query("DELETE FROM genre WHERE type = :type")
    suspend fun deleteAll(type: ItemType)

}