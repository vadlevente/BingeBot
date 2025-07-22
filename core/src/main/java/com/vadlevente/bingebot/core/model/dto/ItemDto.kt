package com.vadlevente.bingebot.core.model.dto

import com.google.gson.annotations.SerializedName
import com.vadlevente.bingebot.core.model.Creator
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
        val genres: List<Genre>?,
        @SerializedName("genre_ids")
        val genreIds: List<Int>?,
        @SerializedName("overview")
        val overview: String,
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
            genreCodes = genres?.map { it.id } ?: genreIds ?: emptyList(),
            overview = overview,
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
        val genres: List<Genre>?,
        @SerializedName("genre_ids")
        val genreIds: List<Int>?,
        val overview: String,
        @SerializedName("poster_path")
        val posterPath: String,
        @SerializedName("vote_average")
        val voteAverage: Float,
        @SerializedName("first_air_date")
        val releaseDate: Date,
        @SerializedName("last_air_date")
        val lastAirDate: Date,
        @SerializedName("created_by")
        val creator: List<Creator>? = null,
        @SerializedName("number_of_episodes")
        val numberOfEpisodes: Int,
        @SerializedName("number_of_seasons")
        val numberOfSeasons: Int,
    ) : ItemDto<Tv> {
        override fun toItem() = Tv(
            id = id,
            title = title,
            originalTitle = originalTitle,
            genreCodes = genres?.map { it.id } ?: genreIds ?: emptyList(),
            overview = overview,
            posterPath = posterPath,
            voteAverage = voteAverage,
            releaseDate = releaseDate,
            lastAirDate = lastAirDate,
            creator = creator?.firstOrNull()?.name,
            numberOfSeasons = numberOfSeasons,
            numberOfEpisodes = numberOfEpisodes,
        )
    }

}
