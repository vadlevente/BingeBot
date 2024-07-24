package com.vadlevente.bingebot.list.domain.usecase.movie

import com.vadlevente.bingebot.core.data.cache.SelectedFiltersCacheDataSource
import com.vadlevente.bingebot.list.domain.usecase.SetIsWatchedFilterUseCase
import com.vadlevente.bingebot.list.domain.usecase.SetIsWatchedFilterUseCaseParams
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetIsWatchedMovieFilterUseCase @Inject constructor(
    private val selectedFiltersCacheDataSource: SelectedFiltersCacheDataSource,
) : SetIsWatchedFilterUseCase {

    override fun execute(params: SetIsWatchedFilterUseCaseParams): Flow<Unit> = emptyFlow {
        selectedFiltersCacheDataSource.updateIsWatched(params.isWatchedSelected)
    }

}