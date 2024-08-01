package com.vadlevente.bingebot.list.domain.usecase

import com.vadlevente.bingebot.core.model.Item

data class ItemListUseCases <T: Item> (
    val getItemsUseCase: GetItemsUseCase<T>,
    val getFiltersUseCase: GetItemFiltersUseCase<T>,
    val setIsWatchedFilterUseCase: SetIsWatchedFilterUseCase<T>,
    val setSelectedGenresUseCase: SetSelectedGenresUseCase<T>,
    val setQueryFilterUseCase: SetQueryFilterUseCase<T>,
    val updateItemsUseCase: UpdateItemsUseCase<T>,
    val updateWatchListsUseCase: UpdateWatchListsUseCase<T>,
)