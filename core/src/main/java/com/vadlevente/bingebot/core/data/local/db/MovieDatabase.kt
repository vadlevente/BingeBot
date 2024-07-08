package com.vadlevente.bingebot.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vadlevente.bingebot.core.data.dao.GenreDao
import com.vadlevente.bingebot.core.data.dao.MovieDao
import com.vadlevente.bingebot.core.model.Genre
import com.vadlevente.bingebot.core.model.Movie

@Database(
    entities = [
        Movie::class,
        Genre::class,
    ],
    version = 1,
)
@TypeConverters(DbTypeConverters::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun genreDao(): GenreDao
}