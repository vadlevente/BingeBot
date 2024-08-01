package com.vadlevente.bingebot.core.model

import androidx.room.Entity
import com.vadlevente.bingebot.core.model.Item.Movie
import com.vadlevente.bingebot.core.model.Item.Tv
import com.vadlevente.bingebot.core.model.ItemType.MOVIE
import com.vadlevente.bingebot.core.model.ItemType.TV

@Entity(tableName = "genre", primaryKeys = ["id", "type"])
data class Genre(
    val id: Int,
    val name: String,
    val type: ItemType,
)

sealed interface GenreFactory <T : Item> {
    fun setType(genre: Genre): Genre

    object MovieGenreFactory : GenreFactory<Movie> {
        override fun setType(genre: Genre) = genre.copy(
            type = MOVIE
        )
    }
    object TvGenreFactory : GenreFactory<Tv> {
        override fun setType(genre: Genre) = genre.copy(
            type = TV
        )
    }
}
