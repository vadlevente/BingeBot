package com.vadlevente.bingebot.bottomsheet.domain.usecases

import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.ui.BaseUseCase

data class SaveItemUseCaseParams(
    val item: Item,
)

interface SaveItemUseCase : BaseUseCase<SaveItemUseCaseParams, Unit>