package com.vadlevente.bingebot.bottomsheet.domain.usecases

import com.vadlevente.bingebot.core.ui.BaseUseCase

data class AddItemToWatchListUseCaseParams(
    val itemId: Int,
    val watchListId: String,
)

interface AddItemToWatchListUseCase : BaseUseCase<AddItemToWatchListUseCaseParams, Boolean>
