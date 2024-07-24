package com.vadlevente.bingebot.list.domain.usecase.movie

import com.vadlevente.bingebot.core.data.cache.SelectedFiltersCacheDataSource
import com.vadlevente.bingebot.list.domain.usecase.SetSelectedGenresUseCase
import com.vadlevente.bingebot.list.domain.usecase.SetSelectedGenresUseCaseParams
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetSelectedMovieGenresUseCase @Inject constructor(
    private val selectedFiltersCacheDataSource: SelectedFiltersCacheDataSource,
) : SetSelectedGenresUseCase {

    override fun execute(params: SetSelectedGenresUseCaseParams): Flow<Unit> = emptyFlow {
        selectedFiltersCacheDataSource.updateGenres(params.genres)
    }

}