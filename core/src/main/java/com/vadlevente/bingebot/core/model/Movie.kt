package com.vadlevente.bingebot.core.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.Date

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey
    val id: Int,
    val title: String,
    @SerializedName("genre_ids")
    val genreCodes: List<Int>,
    val overview: String,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("vote_average")
    val voteAverage: Float,
    @SerializedName("release_date")
    val releaseDate: Date? = null,
    val localeCode: String = SelectedLanguage.default.code,
    val watchedDate: Date? = null,
    val createdDate: Date? = null,
) {
    @Ignore
    val localization: SelectedLanguage = SelectedLanguage.from(localeCode)
}
