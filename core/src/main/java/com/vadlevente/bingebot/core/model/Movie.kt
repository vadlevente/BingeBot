package com.vadlevente.bingebot.core.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.Date

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey
    override val id: Int,
    override val title: String,
    @SerializedName("original_title")
    override val originalTitle: String,
    @SerializedName("genre_ids")
    override val genreCodes: List<Int>,
    override val overview: String,
    @SerializedName("backdrop_path")
    override val backdropPath: String?,
    @SerializedName("poster_path")
    override val posterPath: String?,
    @SerializedName("vote_average")
    override val voteAverage: Float,
    @SerializedName("release_date")
    override val releaseDate: Date? = null,
    val localeCode: String = SelectedLanguage.default.code,
    override val watchedDate: Date? = null,
    override val createdDate: Date? = null,
) : Item {
    @Ignore
    val localization: SelectedLanguage = SelectedLanguage.from(localeCode)
    val isWatched: Boolean
        get() = watchedDate != null
}
