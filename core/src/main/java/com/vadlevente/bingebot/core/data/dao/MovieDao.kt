package com.vadlevente.bingebot.core.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vadlevente.bingebot.core.model.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>)

    @Delete
    suspend fun deleteMovie(movie: Movie)

    @Query("SELECT * from movie")
    fun getAllMovies(): Flow<List<Movie>>

    @Query("SELECT * from movie WHERE localeCode != :localeCode")
    fun getAllIncorrectlyLocalizedMovies(localeCode: String): Flow<List<Movie>>

}