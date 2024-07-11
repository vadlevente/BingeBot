package com.vadlevente.bingebot.core.inject

import com.vadlevente.bingebot.core.data.cache.MovieCacheDataSource
import com.vadlevente.bingebot.core.data.cache.MovieCacheDataSourceImpl
import com.vadlevente.bingebot.core.data.cache.SelectedGenresCacheDataSource
import com.vadlevente.bingebot.core.data.cache.SelectedGenresCacheDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CacheModule {

    @Binds
    @Singleton
    abstract fun bindSelectedGenresCacheDataSource(
        impl: SelectedGenresCacheDataSourceImpl
    ): SelectedGenresCacheDataSource

    @Binds
    @Singleton
    abstract fun bindMovieCacheDataSource(
        impl: MovieCacheDataSourceImpl
    ): MovieCacheDataSource

}