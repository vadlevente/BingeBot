package com.vadlevente.bingebot.core.model.dto

import com.vadlevente.bingebot.core.model.Item

data class ItemsResponseDto<T : Item>(
    val results: List<T>
)

