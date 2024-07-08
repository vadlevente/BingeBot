package com.vadlevente.bingebot.core.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class MovieDetails(
    val id: Int,
    val title: String,
    val genres: List<Genre>,
    val overview: String,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("vote_average")
    val voteAverage: Float,
    @SerializedName("release_date")
    val releaseDate: Date,
) {
    fun toMovie() = Movie(
        id = id,
        title = title,
        genreCodes = genres.map { it.id },
        overview = overview,
        backdropPath = backdropPath,
        posterPath = posterPath,
        voteAverage = voteAverage,
        releaseDate = releaseDate,
    )
}
