package com.vadlevente.bingebot.bottomsheet.domain.usecases

import com.vadlevente.bingebot.core.model.Item

data class ItemBottomSheetUseCases <T: Item>(
    val deleteItemUseCase: DeleteItemUseCase,
    val saveItemUseCase: SaveItemUseCase,
    val setItemSeenUseCase: SetItemSeenUseCase,
    val removeItemFromWatchListUseCase: RemoveItemFromWatchListUseCase,
)
