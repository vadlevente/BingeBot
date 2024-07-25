package com.vadlevente.bingebot.list.domain.usecase

import com.vadlevente.bingebot.core.model.Item

data class ItemListUseCases <T: Item> (
    val getItemsUseCase: GetItemsUseCase<T>,
    val getFiltersUseCase: GetItemFiltersUseCase<T>,
    val setIsWatchedFilterUseCase: SetIsWatchedFilterUseCase,
    val setSelectedGenresUseCase: SetSelectedGenresUseCase,
    val setQueryFilterUseCase: SetQueryFilterUseCase,
    val updateItemsUseCase: UpdateItemsUseCase<T>,
    val updateWatchListsUseCase: UpdateWatchListsUseCase<T>,
)