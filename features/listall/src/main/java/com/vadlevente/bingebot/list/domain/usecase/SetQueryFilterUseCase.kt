package com.vadlevente.bingebot.list.domain.usecase

import com.vadlevente.bingebot.core.ui.BaseUseCase

data class SetQueryFilterUseCaseParams(
    val query: String? = null,
)

interface SetQueryFilterUseCase : BaseUseCase<SetQueryFilterUseCaseParams, Unit>