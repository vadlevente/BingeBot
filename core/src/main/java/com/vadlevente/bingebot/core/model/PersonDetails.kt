package com.vadlevente.bingebot.core.model

import java.util.Date

data class PersonDetails(
    val name: String,
    val birthDay: Date?,
    val deathDay: Date?,
    val profileUrl: String?,
    val castCreditsUpcoming: List<PersonCredit>,
    val castCreditsPrevious: List<PersonCredit>,
    val crewCreditsUpcoming: List<PersonCredit>,
    val crewCreditsPrevious: List<PersonCredit>,
)

data class PersonCredit(
    val creditId: String,
    val id: Int,
    val title: String,
    val posterPath: String?,
    val releaseDate: Date?,
    val voteAverage: Float,
    val info: String,
    val mediaType: MediaType,
    val isFutureRelease: Boolean,
)

enum class MediaType(val code: String) {
    Movie("movie"),
    Tv("tv");

    companion object {
        fun from(value: String) = entries.first { it.code == value }
    }
}
