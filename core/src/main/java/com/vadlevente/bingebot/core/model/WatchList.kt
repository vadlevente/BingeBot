package com.vadlevente.bingebot.core.model

data class WatchList(
    val watchListId: String,
    val title: String,
    val movieIds: List<Int>,
)
