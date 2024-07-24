package com.vadlevente.bingebot.bottomsheet.domain.usecases

import com.vadlevente.bingebot.core.ui.BaseUseCase

data class CreateWatchListUseCaseParams(
    val title: String,
)

interface CreateWatchListUseCase : BaseUseCase<CreateWatchListUseCaseParams, String>