package com.vadlevente.bingebot.bottomsheet.domain.usecases

import com.vadlevente.bingebot.bottomsheet.domain.model.DisplayedOrderBy
import com.vadlevente.bingebot.core.data.cache.SelectedFiltersCacheDataSource
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.model.OrderBy
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetOrderByListUseCase <T : Item> @Inject constructor(
    private val selectedFiltersCacheDataSource: SelectedFiltersCacheDataSource<T>,
) : BaseUseCase<Unit, List<DisplayedOrderBy>> {

    override fun execute(params: Unit): Flow<List<DisplayedOrderBy>> =
        selectedFiltersCacheDataSource.filtersState.map {
            listOf(
                DisplayedOrderBy(
                    orderBy = OrderBy.DATE_ADDED_DESCENDING,
                    isSelected = it.orderBy == OrderBy.DATE_ADDED_DESCENDING
                ),
                DisplayedOrderBy(
                    orderBy = OrderBy.DATE_ADDED_ASCENDING,
                    isSelected = it.orderBy == OrderBy.DATE_ADDED_ASCENDING
                ),
                DisplayedOrderBy(
                    orderBy = OrderBy.RATING_DESCENDING,
                    isSelected = it.orderBy == OrderBy.RATING_DESCENDING
                ),
                DisplayedOrderBy(
                    orderBy = OrderBy.RATING_ASCENDING,
                    isSelected = it.orderBy == OrderBy.RATING_ASCENDING
                ),
            )
        }

}