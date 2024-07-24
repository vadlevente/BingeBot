package com.vadlevente.bingebot.list.domain.usecase

import com.vadlevente.bingebot.core.model.DisplayedItem
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.ui.BaseUseCase

data class GetItemsUseCaseParams(
    val query: String? = null,
)

interface GetItemsUseCase <T: Item> : BaseUseCase<GetItemsUseCaseParams, List<DisplayedItem<T>>>