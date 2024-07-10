package com.vadlevente.bingebot.core.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchList")
data class WatchList(
    @PrimaryKey
    val watchListId: String,
    val title: String,
    val movieIds: List<Int>,
) {
    constructor(): this("", "", emptyList())
}
