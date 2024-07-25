package com.vadlevente.bingebot.list.domain.usecase

import com.vadlevente.bingebot.core.data.cache.SelectedFiltersCacheDataSource
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class SetQueryFilterUseCaseParams(
    val query: String? = null,
)

class SetQueryFilterUseCase @Inject constructor(
    private val selectedFiltersCacheDataSource: SelectedFiltersCacheDataSource,
) : BaseUseCase<SetQueryFilterUseCaseParams, Unit> {

    override fun execute(params: SetQueryFilterUseCaseParams): Flow<Unit> = emptyFlow {
        selectedFiltersCacheDataSource.updateQuery(params.query)
    }

}