package com.vadlevente.bingebot.core.inject

import com.vadlevente.bingebot.core.data.repository.ConfigurationRepository
import com.vadlevente.bingebot.core.data.repository.ConfigurationRepositoryImpl
import com.vadlevente.bingebot.core.data.repository.ItemRepository
import com.vadlevente.bingebot.core.data.repository.ItemRepositoryImpl
import com.vadlevente.bingebot.core.data.repository.PersonRepository
import com.vadlevente.bingebot.core.data.repository.PersonRepositoryImpl
import com.vadlevente.bingebot.core.model.Item.Movie
import com.vadlevente.bingebot.core.model.Item.Tv
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        impl: ItemRepositoryImpl<Movie>
    ): ItemRepository<Movie>

    @Binds
    @Singleton
    abstract fun bindTvRepository(
        impl: ItemRepositoryImpl<Tv>
    ): ItemRepository<Tv>

    @Binds
    @Singleton
    abstract fun bindConfigurationRepository(
        impl: ConfigurationRepositoryImpl
    ): ConfigurationRepository

    @Binds
    @Singleton
    abstract fun bindPersonRepository(
        impl: PersonRepositoryImpl
    ): PersonRepository

}