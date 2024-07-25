package com.vadlevente.bingebot.core.inject

import com.vadlevente.bingebot.core.data.cache.ItemCacheDataSource
import com.vadlevente.bingebot.core.data.cache.ItemCacheDataSourceImpl
import com.vadlevente.bingebot.core.data.cache.SelectedFiltersCacheDataSource
import com.vadlevente.bingebot.core.data.cache.SelectedFiltersCacheDataSourceImpl
import com.vadlevente.bingebot.core.model.Item.Movie
import com.vadlevente.bingebot.core.model.Item.Tv
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
        impl: ItemCacheDataSourceImpl<Movie>
    ): ItemCacheDataSource<Movie>

    @Binds
    @Singleton
    abstract fun bindTvCacheDataSource(
        impl: ItemCacheDataSourceImpl<Tv>
    ): ItemCacheDataSource<Tv>


}