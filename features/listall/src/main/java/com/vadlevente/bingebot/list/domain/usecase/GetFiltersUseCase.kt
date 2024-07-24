package com.vadlevente.bingebot.list.domain.usecase

import com.vadlevente.bingebot.core.ui.BaseUseCase
import com.vadlevente.bingebot.list.domain.model.DisplayedFilters

interface GetFiltersUseCase : BaseUseCase<Unit, DisplayedFilters>