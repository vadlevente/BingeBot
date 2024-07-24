package com.vadlevente.bingebot.list.domain.usecase

import com.vadlevente.bingebot.core.model.DisplayedItem
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.ui.BaseUseCase

interface GetItemsUseCase <T: Item> : BaseUseCase<Unit, List<DisplayedItem<T>>>