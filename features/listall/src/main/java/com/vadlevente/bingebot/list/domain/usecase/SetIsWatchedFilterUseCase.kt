package com.vadlevente.bingebot.list.domain.usecase

import com.vadlevente.bingebot.core.data.cache.SelectedFiltersCacheDataSource
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class SetIsWatchedFilterUseCaseParams(
    val isWatchedSelected: Boolean? = null,
)

class SetIsWatchedFilterUseCase <T : Item> @Inject constructor(
    private val selectedFiltersCacheDataSource: SelectedFiltersCacheDataSource<T>,
) : BaseUseCase<SetIsWatchedFilterUseCaseParams, Unit> {

    override fun execute(params: SetIsWatchedFilterUseCaseParams): Flow<Unit> = emptyFlow {
        selectedFiltersCacheDataSource.updateIsWatched(params.isWatchedSelected)
    }

}