package com.vadlevente.bingebot.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vadlevente.bingebot.core.data.dao.GenreDao
import com.vadlevente.bingebot.core.data.dao.MovieDao
import com.vadlevente.bingebot.core.data.dao.WatchListDao
import com.vadlevente.bingebot.core.model.Genre
import com.vadlevente.bingebot.core.model.Item.Movie
import com.vadlevente.bingebot.core.model.WatchList

@Database(
    entities = [
        Movie::class,
        Genre::class,
        WatchList::class,
    ],
    version = 1,
)
@TypeConverters(DbTypeConverters::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun genreDao(): GenreDao
    abstract fun watchListDao(): WatchListDao
}