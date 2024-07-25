package com.vadlevente.bingebot.core.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vadlevente.bingebot.core.model.Item.Movie
import com.vadlevente.bingebot.core.model.Item.Tv
import com.vadlevente.bingebot.core.model.ItemType.MOVIE
import com.vadlevente.bingebot.core.model.ItemType.TV

@Entity(tableName = "watchList")
data class WatchList(
    @PrimaryKey
    val watchListId: String,
    val title: String,
    val itemIds: List<Int>,
    val type: ItemType,
) {
    constructor() : this("", "", emptyList(), MOVIE)

    companion object {
        inline fun <reified T : Item> create(
            watchListId: String,
            title: String,
        ) = WatchList(
            watchListId = watchListId,
            title = title,
            itemIds = emptyList(),
            type = when (T::class.java) {
                Movie::class.java -> MOVIE
                else -> TV
            }
        )
    }

}

sealed interface WatchListFactory <T : Item> {
    fun create(watchListId: String, title: String): WatchList

    object MovieWatchListFactory : WatchListFactory<Movie> {
        override fun create(
            watchListId: String,
            title: String,
        ) = WatchList(
            watchListId = watchListId,
            title = title,
            itemIds = emptyList(),
            type = MOVIE
        )
    }

    object TvWatchListFactory : WatchListFactory<Tv> {
        override fun create(
            watchListId: String,
            title: String,
        ) = WatchList(
            watchListId = watchListId,
            title = title,
            itemIds = emptyList(),
            type = TV
        )
    }
}

