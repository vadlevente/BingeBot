package com.vadlevente.bingebot.core.model.dto

import com.google.gson.annotations.SerializedName
import com.vadlevente.bingebot.core.model.Genre
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.model.Item.Movie
import com.vadlevente.bingebot.core.model.Item.Tv
import java.util.Date

sealed interface ItemDto<T : Item> {

    fun toItem(): T

    data class MovieDto(
        val id: Int,
        @SerializedName("title")
        val title: String,
        @SerializedName("original_title")
        val originalTitle: String,
        val genres: List<Genre>,
        @SerializedName("overview")
        val overview: String,
        @SerializedName("backdrop_path")
        val backdropPath: String,
        @SerializedName("poster_path")
        val posterPath: String,
        @SerializedName("vote_average")
        val voteAverage: Float,
        @SerializedName("release_date")
        val releaseDate: Date,
        @SerializedName("budget")
        val budget: Long?,
        @SerializedName("revenue")
        val revenue: Long?,
        @SerializedName("runtime")
        val runtime: Long?,
    ) : ItemDto<Movie> {
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
            budget = budget,
            revenue = revenue,
            runtime = runtime
        )
    }

    data class TvDto(
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
    ) : ItemDto<Tv> {
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
