package com.vadlevente.bingebot.list.domain.usecase.movie

import com.vadlevente.bingebot.core.data.cache.SelectedFiltersCacheDataSource
import com.vadlevente.bingebot.list.domain.usecase.SetQueryFilterUseCase
import com.vadlevente.bingebot.list.domain.usecase.SetQueryFilterUseCaseParams
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetQueryMovieFilterUseCase @Inject constructor(
    private val selectedFiltersCacheDataSource: SelectedFiltersCacheDataSource,
) : SetQueryFilterUseCase {

    override fun execute(params: SetQueryFilterUseCaseParams): Flow<Unit> = emptyFlow {
        selectedFiltersCacheDataSource.updateQuery(params.query)
    }

}