package com.vadlevente.bingebot.bottomsheet.domain.usecases

import com.vadlevente.bingebot.core.data.cache.SelectedFiltersCacheDataSource
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.model.OrderBy
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class SetOrderByFilterUseCaseParams(
    val orderBy: OrderBy,
)

class SetOrderByFilterUseCase <T : Item> @Inject constructor(
    private val selectedFiltersCacheDataSource: SelectedFiltersCacheDataSource<T>,
) : BaseUseCase<SetOrderByFilterUseCaseParams, Unit> {

    override fun execute(params: SetOrderByFilterUseCaseParams): Flow<Unit> = emptyFlow {
        selectedFiltersCacheDataSource.updateOrderBy(params.orderBy)
    }

}