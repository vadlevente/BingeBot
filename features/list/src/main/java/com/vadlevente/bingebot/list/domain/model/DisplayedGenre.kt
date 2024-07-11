package com.vadlevente.bingebot.list.domain.model

import com.vadlevente.bingebot.core.model.Genre

data class DisplayedGenre(
    val genre: Genre,
    val isSelected: Boolean,
)
