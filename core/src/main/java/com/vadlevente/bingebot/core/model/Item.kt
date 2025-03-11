package com.vadlevente.bingebot.core.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.vadlevente.bingebot.core.model.Item.Movie
import com.vadlevente.bingebot.core.model.Item.Tv
import java.util.Date

data class DisplayedItem<T: Item>(
    val item: T,
    val thumbnailUrl: String?,
)

sealed interface Item {
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
    val localeCode: String
    val isWatched: Boolean

    fun <T : Item> copyLocale(
        localeCode: String
    ) = when (this) {
        is Movie -> copy(localeCode = localeCode) as T
        is Tv -> copy(localeCode = localeCode) as T
    }

    fun <T : Item> copyWatchedDate(
        watchedDate: Date?
    ) = when (this) {
        is Movie -> copy(watchedDate = watchedDate) as T
        is Tv -> copy(watchedDate = watchedDate) as T
    }

    fun <T : Item> copyCreatedDate(
        createdDate: Date
    ) = when (this) {
        is Movie -> copy(createdDate = createdDate) as T
        is Tv -> copy(createdDate = createdDate) as T
    }

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
        @SerializedName("budget")
        val budget: Long?,
        @SerializedName("revenue")
        val revenue: Long?,
        @SerializedName("runtime")
        val runtime: Long?,
        @SerializedName("release_date")
        override val releaseDate: Date? = null,
        override val localeCode: String = SelectedLanguage.default.code,
        override val watchedDate: Date? = null,
        override val createdDate: Date? = null,
    ) : Item {
        @Ignore
        val localization: SelectedLanguage = SelectedLanguage.from(localeCode)
        override val isWatched: Boolean
            get() = watchedDate != null
    }

    @Entity(tableName = "tv")
    data class Tv(
        @PrimaryKey
        override val id: Int,
        @SerializedName("name")
        override val title: String,
        @SerializedName("original_name")
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
        @SerializedName("first_air_date")
        override val releaseDate: Date? = null,
        @SerializedName("created_by")
        val creator: String? = null,
        @SerializedName("number_of_episodes")
        val numberOfEpisodes: Int = 0,
        @SerializedName("number_of_seasons")
        val numberOfSeasons: Int = 0,
        @SerializedName("last_air_date")
        val lastAirDate: Date? = null,
        override val localeCode: String = SelectedLanguage.default.code,
        override val watchedDate: Date? = null,
        override val createdDate: Date? = null,
    ) : Item {
        @Ignore
        val localization: SelectedLanguage = SelectedLanguage.from(localeCode)
        override val isWatched: Boolean
            get() = watchedDate != null
    }
}

sealed interface SkeletonFactory <T : Item> {
    fun createSkeleton(): T
    object MovieSkeletonFactory : SkeletonFactory<Movie> {
        override fun createSkeleton() = Movie(
            id = 1,
            title = "",
            originalTitle = "",
            genreCodes = emptyList(),
            overview = "",
            backdropPath = null,
            posterPath = null,
            voteAverage = 1f,
            budget = null,
            revenue = null,
            runtime = null,
        )
    }
    object TvSkeletonFactory : SkeletonFactory<Tv> {
        override fun createSkeleton() = Tv(
            id = 1,
            title = "",
            originalTitle = "",
            genreCodes = emptyList(),
            overview = "",
            backdropPath = null,
            posterPath = null,
            voteAverage = 1f,
        )
    }
}
