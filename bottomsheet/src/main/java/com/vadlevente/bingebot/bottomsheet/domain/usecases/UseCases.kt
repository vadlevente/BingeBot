package com.vadlevente.bingebot.bottomsheet.domain.usecases

import com.vadlevente.bingebot.core.model.Item

data class ItemBottomSheetUseCases <T: Item>(
    val deleteItemUseCase: DeleteItemUseCase<T>,
    val saveItemUseCase: SaveItemUseCase<T>,
    val setItemSeenUseCase: SetItemSeenUseCase<T>,
    val removeItemFromWatchListUseCase: RemoveItemFromWatchListUseCase<T>,
)
