package com.vadlevente.bingebot.bottomsheet.domain.usecases

import com.vadlevente.bingebot.core.model.WatchList
import com.vadlevente.bingebot.core.ui.BaseUseCase

data class GetWatchListsUseCaseParams(
    val itemId: Int? = null,
)

interface GetWatchListsUseCase : BaseUseCase<GetWatchListsUseCaseParams, List<WatchList>>