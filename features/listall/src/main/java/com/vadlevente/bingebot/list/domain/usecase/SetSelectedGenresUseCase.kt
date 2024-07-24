package com.vadlevente.bingebot.list.domain.usecase

import com.vadlevente.bingebot.core.model.Genre
import com.vadlevente.bingebot.core.ui.BaseUseCase

data class SetSelectedGenresUseCaseParams(
    val genres: List<Genre>,
)

interface SetSelectedGenresUseCase : BaseUseCase<SetSelectedGenresUseCaseParams, Unit>
