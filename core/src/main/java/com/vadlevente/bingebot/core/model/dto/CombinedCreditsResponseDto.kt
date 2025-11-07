package com.vadlevente.bingebot.core.model.dto

import com.google.gson.annotations.SerializedName
import java.util.Date

data class CombinedCreditsResponseDto(
    @SerializedName("cast")
    val castCredits: List<CastCredit>,
    @SerializedName("crew")
    val crewCredits: List<CrewCredit>,
)

data class CastCredit(
    @SerializedName("credit_id")
    val creditId: String,
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String?,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: Date?,
    @SerializedName("vote_average")
    val voteAverage: Float,
    @SerializedName("character")
    val character: String,
    @SerializedName("media_type")
    val mediaType: String,
    @SerializedName("name")
    val name: String?,
    @SerializedName("first_credit_air_date")
    val firstCreditAirDate: String?,
)

data class CrewCredit(
    @SerializedName("credit_id")
    val creditId: String,
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String?,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: Date?,
    @SerializedName("vote_average")
    val voteAverage: Float,
    @SerializedName("job")
    val job: String,
    @SerializedName("media_type")
    val mediaType: String,
    @SerializedName("name")
    val name: String?,
    @SerializedName("first_credit_air_date")
    val firstCreditAirDate: String?,
)