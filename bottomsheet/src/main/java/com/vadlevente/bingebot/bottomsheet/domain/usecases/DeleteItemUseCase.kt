package com.vadlevente.bingebot.bottomsheet.domain.usecases

import com.vadlevente.bingebot.core.ui.BaseUseCase

data class DeleteItemUseCaseParams(
    val itemId: Int,
)

interface DeleteItemUseCase : BaseUseCase<DeleteItemUseCaseParams, Unit>