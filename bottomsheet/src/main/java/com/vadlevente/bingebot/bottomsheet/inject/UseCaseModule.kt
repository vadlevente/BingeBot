package com.vadlevente.bingebot.bottomsheet.inject

import com.vadlevente.bingebot.bottomsheet.domain.usecases.DeleteItemUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.ItemBottomSheetUseCases
import com.vadlevente.bingebot.bottomsheet.domain.usecases.RemoveItemFromWatchListUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.SaveItemUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.SetItemSeenUseCase
import com.vadlevente.bingebot.core.model.Item.Movie
import com.vadlevente.bingebot.core.model.Item.Tv
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
        deleteItemUseCase: DeleteItemUseCase<Movie>,
        saveItemUseCase: SaveItemUseCase<Movie>,
        setItemSeenUseCase: SetItemSeenUseCase<Movie>,
        removeItemFromWatchListUseCase: RemoveItemFromWatchListUseCase<Movie>,
    ): ItemBottomSheetUseCases<Movie> = ItemBottomSheetUseCases(
        deleteItemUseCase,
        saveItemUseCase,
        setItemSeenUseCase,
        removeItemFromWatchListUseCase,
    )

    @Singleton
    @Provides
    fun provideTvBottomSheetUseCases(
        deleteItemUseCase: DeleteItemUseCase<Tv>,
        saveItemUseCase: SaveItemUseCase<Tv>,
        setItemSeenUseCase: SetItemSeenUseCase<Tv>,
        removeItemFromWatchListUseCase: RemoveItemFromWatchListUseCase<Tv>,
    ): ItemBottomSheetUseCases<Tv> = ItemBottomSheetUseCases(
        deleteItemUseCase,
        saveItemUseCase,
        setItemSeenUseCase,
        removeItemFromWatchListUseCase,
    )

}