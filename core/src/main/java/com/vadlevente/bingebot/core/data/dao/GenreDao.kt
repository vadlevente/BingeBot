package com.vadlevente.bingebot.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vadlevente.bingebot.core.model.Genre
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao {

    @Query("SELECT * from genre")
    fun getAllGenres(): Flow<List<Genre>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<Genre>)

    @Query("DELETE FROM genre")
    suspend fun deleteAll()

}