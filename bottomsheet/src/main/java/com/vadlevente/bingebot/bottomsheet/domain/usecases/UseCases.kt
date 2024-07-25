package com.vadlevente.bingebot.bottomsheet.domain.usecases

import com.vadlevente.bingebot.bottomsheet.domain.usecases.movie.DeleteItemUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.movie.RemoveItemFromWatchListUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.movie.SaveItemUseCase
import com.vadlevente.bingebot.bottomsheet.domain.usecases.movie.SetItemSeenUseCase
import com.vadlevente.bingebot.core.model.Item

data class ItemBottomSheetUseCases <T: Item>(
    val deleteItemUseCase: DeleteItemUseCase<T>,
    val saveItemUseCase: SaveItemUseCase<T>,
    val setItemSeenUseCase: SetItemSeenUseCase<T>,
    val removeItemFromWatchListUseCase: RemoveItemFromWatchListUseCase<T>,
)
