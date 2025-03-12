package com.vadlevente.bingebot.core.model.dto

import com.vadlevente.bingebot.core.model.Item

data class ItemsResponseDto<TItem : Item, TDto : ItemDto<TItem>>(
    val results: List<TDto>
)

