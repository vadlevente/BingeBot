package com.vadlevente.bingebot.bottomsheet.domain.usecases

import com.vadlevente.bingebot.core.ui.BaseUseCase

data class RemoveItemFromWatchListUseCaseParams(
    val itemId: Int,
    val watchListId: String,
)

interface RemoveItemFromWatchListUseCase : BaseUseCase<RemoveItemFromWatchListUseCaseParams, Unit>