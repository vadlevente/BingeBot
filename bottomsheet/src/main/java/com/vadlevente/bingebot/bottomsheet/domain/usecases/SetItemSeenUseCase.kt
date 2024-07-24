package com.vadlevente.bingebot.bottomsheet.domain.usecases

import com.vadlevente.bingebot.core.ui.BaseUseCase
import java.util.Date

data class SetItemSeenUseCaseParams(
    val itemId: Int,
    val watchedDate: Date?,
)

interface SetItemSeenUseCase : BaseUseCase<SetItemSeenUseCaseParams, Unit>