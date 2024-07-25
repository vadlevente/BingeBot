package com.vadlevente.bingebot.core.model

import com.google.gson.annotations.SerializedName
import com.vadlevente.bingebot.core.model.Item.Movie
import com.vadlevente.bingebot.core.model.Item.Tv
import java.util.Date

sealed interface ItemDetails <T: Item> {

    fun toItem(): T

    data class MovieDetails(
        val id: Int,
        val title: String,
        @SerializedName("original_title")
        val originalTitle: String,
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
    ) : ItemDetails<Movie> {
        override fun toItem() = Movie(
            id = id,
            title = title,
            originalTitle = originalTitle,
            genreCodes = genres.map { it.id },
            overview = overview,
            backdropPath = backdropPath,
            posterPath = posterPath,
            voteAverage = voteAverage,
            releaseDate = releaseDate,
        )
    }

    data class TvDetails(
        val id: Int,
        @SerializedName("name")
        val title: String,
        @SerializedName("original_name")
        val originalTitle: String,
        val genres: List<Genre>,
        val overview: String,
        @SerializedName("backdrop_path")
        val backdropPath: String,
        @SerializedName("poster_path")
        val posterPath: String,
        @SerializedName("vote_average")
        val voteAverage: Float,
        @SerializedName("first_air_date")
        val releaseDate: Date,
    ) : ItemDetails<Tv> {
        override fun toItem() = Tv(
            id = id,
            title = title,
            originalTitle = originalTitle,
            genreCodes = genres.map { it.id },
            overview = overview,
            backdropPath = backdropPath,
            posterPath = posterPath,
            voteAverage = voteAverage,
            releaseDate = releaseDate,
        )
    }

}
