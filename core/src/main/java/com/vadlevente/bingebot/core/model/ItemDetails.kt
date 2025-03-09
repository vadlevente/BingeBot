package com.vadlevente.bingebot.core.model

data class ItemDetails<T : Item>(
    val item: T,
    val credits: Credits,
)
