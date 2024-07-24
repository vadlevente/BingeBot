package com.vadlevente.bingebot.list.inject

import com.vadlevente.bingebot.core.model.Movie
import com.vadlevente.bingebot.list.domain.usecase.ItemListUseCases
import com.vadlevente.bingebot.list.domain.usecase.movie.GetMovieFiltersUseCase
import com.vadlevente.bingebot.list.domain.usecase.movie.GetMoviesUseCase
import com.vadlevente.bingebot.list.domain.usecase.movie.SetIsWatchedMovieFilterUseCase
import com.vadlevente.bingebot.list.domain.usecase.movie.SetQueryMovieFilterUseCase
import com.vadlevente.bingebot.list.domain.usecase.movie.SetSelectedMovieGenresUseCase
import com.vadlevente.bingebot.list.domain.usecase.movie.UpdateMovieWatchListsUseCase
import com.vadlevente.bingebot.list.domain.usecase.movie.UpdateMoviesUseCase
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
        getItemsUseCase: GetMoviesUseCase,
        getFiltersUseCase: GetMovieFiltersUseCase,
        setIsWatchedFilterUseCase: SetIsWatchedMovieFilterUseCase,
        setSelectedGenresUseCase: SetSelectedMovieGenresUseCase,
        setQueryFilterUseCase: SetQueryMovieFilterUseCase,
        updateItemsUseCase: UpdateMoviesUseCase,
        updateWatchListsUseCase: UpdateMovieWatchListsUseCase,
    ): ItemListUseCases<Movie> = ItemListUseCases(
        getItemsUseCase,
        getFiltersUseCase,
        setIsWatchedFilterUseCase,
        setSelectedGenresUseCase,
        setQueryFilterUseCase,
        updateItemsUseCase,
        updateWatchListsUseCase,
    )

}