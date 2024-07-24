package com.vadlevente.bingebot.bottomsheet.inject

import com.vadlevente.bingebot.bottomsheet.domain.usecases.ItemBottomSheetUseCases
import com.vadlevente.bingebot.bottomsheet.domain.usecases.movie.DeleteMovieUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.movie.RemoveMovieFromWatchListUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.movie.SaveMovieUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.movie.SetMovieSeenUseCase
import com.vadlevente.bingebot.core.model.Movie
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
    fun provideMovieBottomSheetUseCases(
        deleteMovieUseCase: DeleteMovieUseCase,
        saveMovieUseCase: SaveMovieUseCase,
        setMovieSeenUseCase: SetMovieSeenUseCase,
        removeMovieFromWatchListUseCase: RemoveMovieFromWatchListUseCase,
    ): ItemBottomSheetUseCases<Movie> = ItemBottomSheetUseCases(
        deleteMovieUseCase,
        saveMovieUseCase,
        setMovieSeenUseCase,
        removeMovieFromWatchListUseCase,
    )

}