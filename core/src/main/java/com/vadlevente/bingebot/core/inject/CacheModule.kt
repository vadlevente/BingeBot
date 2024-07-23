package com.vadlevente.bingebot.core.inject

import com.vadlevente.bingebot.core.data.cache.MovieCacheDataSource
import com.vadlevente.bingebot.core.data.cache.MovieCacheDataSourceImpl
import com.vadlevente.bingebot.core.data.cache.SelectedFiltersCacheDataSource
import com.vadlevente.bingebot.core.data.cache.SelectedFiltersCacheDataSourceImpl
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
    abstract fun bindSelectedFiltersCacheDataSource(
        impl: SelectedFiltersCacheDataSourceImpl
    ): SelectedFiltersCacheDataSource

    @Binds
    @Singleton
    abstract fun bindMovieCacheDataSource(
        impl: MovieCacheDataSourceImpl
    ): MovieCacheDataSource

}