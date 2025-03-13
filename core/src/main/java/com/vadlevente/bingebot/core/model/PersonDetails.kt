package com.vadlevente.bingebot.core.model

import java.util.Date

data class PersonDetails(
    val name: String,
    val birthDay: Date?,
    val deathDay: Date?,
    val profileUrl: String?,
    val castCredits: List<PersonCredit>,
    val crewCredits: List<PersonCredit>,
)

data class PersonCredit(
    val id: Int,
    val title: String,
    val posterPath: String?,
    val releaseDate: Date,
    val voteAverage: Float,
    val info: String,
    val mediaType: MediaType,
)

enum class MediaType(val code: String) {
    Movie("movie"),
    Tv("tv");

    companion object {
        fun from(value: String) = entries.first { it.code == value }
    }
}
