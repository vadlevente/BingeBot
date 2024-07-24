package com.vadlevente.bingebot.list.domain.usecase

import com.vadlevente.bingebot.core.model.Item

data class ItemListUseCases <T: Item> (
    val getItemsUseCase: GetItemsUseCase<T>,
    val getFiltersUseCase: GetFiltersUseCase,
    val setIsWatchedFilterUseCase: SetIsWatchedFilterUseCase,
    val setSelectedGenresUseCase: SetSelectedGenresUseCase,
    val updateItemsUseCase: UpdateItemsUseCase,
    val updateWatchListsUseCase: UpdateWatchListsUseCase,
)