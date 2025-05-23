package com.vadlevente.bingebot.core.inject

import android.app.Application
import androidx.room.Room
import com.vadlevente.bingebot.core.data.dao.GenreDao
import com.vadlevente.bingebot.core.data.dao.MovieDao
import com.vadlevente.bingebot.core.data.dao.TvDao
import com.vadlevente.bingebot.core.data.dao.WatchListDao
import com.vadlevente.bingebot.core.data.local.db.DbTypeConverters
import com.vadlevente.bingebot.core.data.local.db.MovieDatabase
import com.vadlevente.bingebot.core.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    @Singleton
    fun provideDatabase(
        application: Application,
        typeConverters: DbTypeConverters,
    ): MovieDatabase =
        Room.databaseBuilder(application,
            MovieDatabase::class.java, DATABASE_NAME)
            .addTypeConverter(typeConverters)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideMovieDao(
        db: MovieDatabase
    ): MovieDao = db.movieDao()

    @Provides
    @Singleton
    fun provideTvDao(
        db: MovieDatabase
    ): TvDao = db.tvDao()

    @Provides
    @Singleton
    fun provideGenreDao(
        db: MovieDatabase
    ): GenreDao = db.genreDao()

    @Provides
    @Singleton
    fun provideWatchListDao(
        db: MovieDatabase
    ): WatchListDao = db.watchListDao()



}