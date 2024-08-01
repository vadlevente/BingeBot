package com.vadlevente.bingebot.core.inject

import com.vadlevente.bingebot.core.data.local.db.ItemLocalDataSource
import com.vadlevente.bingebot.core.data.local.db.MovieLocalDataSource
import com.vadlevente.bingebot.core.data.local.db.TvLocalDataSource
import com.vadlevente.bingebot.core.data.remote.ItemRemoteDataSource
import com.vadlevente.bingebot.core.data.remote.MovieRemoteDataSource
import com.vadlevente.bingebot.core.data.remote.TvRemoteDataSource
import com.vadlevente.bingebot.core.data.remote.firestore.FirestoreItemDataSource
import com.vadlevente.bingebot.core.data.remote.firestore.FirestoreItemDataSourceImpl
import com.vadlevente.bingebot.core.model.Item.Movie
import com.vadlevente.bingebot.core.model.Item.Tv
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindMovieLocalDataSource(
        impl: MovieLocalDataSource
    ): ItemLocalDataSource<Movie>

    @Singleton
    @Binds
    abstract fun bindTvLocalDataSource(
        impl: TvLocalDataSource
    ): ItemLocalDataSource<Tv>

    @Singleton
    @Binds
    abstract fun bindFirestoreMovieDataSource(
        impl: FirestoreItemDataSourceImpl<Movie>
    ): FirestoreItemDataSource<Movie>

    @Singleton
    @Binds
    abstract fun bindFirestoreTvDataSource(
        impl: FirestoreItemDataSourceImpl<Tv>
    ): FirestoreItemDataSource<Tv>

    @Singleton
    @Binds
    abstract fun bindMovieRemoteDataSource(
        impl: MovieRemoteDataSource
    ): ItemRemoteDataSource<Movie>

    @Singleton
    @Binds
    abstract fun bindTvRemoteDataSource(
        impl: TvRemoteDataSource
    ): ItemRemoteDataSource<Tv>

}