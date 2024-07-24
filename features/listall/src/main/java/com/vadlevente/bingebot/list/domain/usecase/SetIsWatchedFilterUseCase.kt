package com.vadlevente.bingebot.list.domain.usecase

import com.vadlevente.bingebot.core.ui.BaseUseCase

data class SetIsWatchedFilterUseCaseParams(
    val isWatchedSelected: Boolean? = null,
)

interface SetIsWatchedFilterUseCase : BaseUseCase<SetIsWatchedFilterUseCaseParams, Unit>