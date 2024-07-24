package com.vadlevente.bingebot.core.model

import java.util.Date

data class DisplayedItem<T: Item>(
    val item: T,
    val thumbnailUrl: String?,
)

interface Item {
    val id: Int
    val title: String
    val originalTitle: String
    val genreCodes: List<Int>
    val overview: String
    val backdropPath: String?
    val posterPath: String?
    val voteAverage: Float
    val releaseDate: Date?
    val watchedDate: Date?
    val createdDate: Date?
}
