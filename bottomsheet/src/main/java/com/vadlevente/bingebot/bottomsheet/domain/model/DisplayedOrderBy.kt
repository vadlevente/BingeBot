package com.vadlevente.bingebot.bottomsheet.domain.model

import com.vadlevente.bingebot.core.model.OrderBy

data class DisplayedOrderBy(
    val orderBy: OrderBy,
    val isSelected: Boolean
)
