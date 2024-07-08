package com.vadlevente.bingebot.core.inject

import android.app.Application
import androidx.room.Room
import com.vadlevente.bingebot.core.data.dao.GenreDao
import com.vadlevente.bingebot.core.data.dao.MovieDao
import com.vadlevente.bingebot.core.data.local.db.DbTypeConverters
import com.vadlevente.bingebot.core.data.local.db.MovieDatabase
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
            MovieDatabase::class.java, "movieDatabase.db")
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
    fun provideGenreDao(
        db: MovieDatabase
    ): GenreDao = db.genreDao()

}