package com.vadlevente.bingebot.list.inject

import com.vadlevente.bingebot.core.model.Item.Movie
import com.vadlevente.bingebot.core.model.Item.Tv
import com.vadlevente.bingebot.core.usecase.SetDisplayNextToWatchUseCase
import com.vadlevente.bingebot.list.domain.usecase.GetItemFiltersUseCase
import com.vadlevente.bingebot.list.domain.usecase.GetItemsUseCase
import com.vadlevente.bingebot.list.domain.usecase.GetNextItemsToWatchUseCase
import com.vadlevente.bingebot.list.domain.usecase.ItemListUseCases
import com.vadlevente.bingebot.list.domain.usecase.SetIsWatchedFilterUseCase
import com.vadlevente.bingebot.list.domain.usecase.SetQueryFilterUseCase
import com.vadlevente.bingebot.list.domain.usecase.SetSelectedGenresUseCase
import com.vadlevente.bingebot.list.domain.usecase.UpdateItemsUseCase
import com.vadlevente.bingebot.list.domain.usecase.UpdateWatchListsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {


    @Singleton
    @Provides
    fun provideMovieListUseCases(
        getItemsUseCase: GetItemsUseCase<Movie>,
        getFiltersUseCase: GetItemFiltersUseCase<Movie>,
        setIsWatchedFilterUseCase: SetIsWatchedFilterUseCase<Movie>,
        setSelectedGenresUseCase: SetSelectedGenresUseCase<Movie>,
        setQueryFilterUseCase: SetQueryFilterUseCase<Movie>,
        updateItemsUseCase: UpdateItemsUseCase<Movie>,
        updateWatchListsUseCase: UpdateWatchListsUseCase<Movie>,
        setDisplayNextToWatchUseCase: SetDisplayNextToWatchUseCase,
        getNextItemsToWatchUseCase: GetNextItemsToWatchUseCase<Movie>,
    ): ItemListUseCases<Movie> = ItemListUseCases(
        getItemsUseCase,
        getFiltersUseCase,
        setIsWatchedFilterUseCase,
        setSelectedGenresUseCase,
        setQueryFilterUseCase,
        updateItemsUseCase,
        updateWatchListsUseCase,
        setDisplayNextToWatchUseCase,
        getNextItemsToWatchUseCase,
    )

    @Singleton
    @Provides
    fun provideTvListUseCases(
        getItemsUseCase: GetItemsUseCase<Tv>,
        getFiltersUseCase: GetItemFiltersUseCase<Tv>,
        setIsWatchedFilterUseCase: SetIsWatchedFilterUseCase<Tv>,
        setSelectedGenresUseCase: SetSelectedGenresUseCase<Tv>,
        setQueryFilterUseCase: SetQueryFilterUseCase<Tv>,
        updateItemsUseCase: UpdateItemsUseCase<Tv>,
        updateWatchListsUseCase: UpdateWatchListsUseCase<Tv>,
        setDisplayNextToWatchUseCase: SetDisplayNextToWatchUseCase,
        getNextItemsToWatchUseCase: GetNextItemsToWatchUseCase<Tv>,
    ): ItemListUseCases<Tv> = ItemListUseCases(
        getItemsUseCase,
        getFiltersUseCase,
        setIsWatchedFilterUseCase,
        setSelectedGenresUseCase,
        setQueryFilterUseCase,
        updateItemsUseCase,
        updateWatchListsUseCase,
        setDisplayNextToWatchUseCase,
        getNextItemsToWatchUseCase,
    )

}