package com.vadlevente.bingebot.core.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
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
    val overview: String?
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
        override val originalTitle: String,
        override val genreCodes: List<Int>,
        @Ignore
        override val overview: String? = null,
        override val posterPath: String?,
        override val voteAverage: Float,
        @Ignore
        val budget: Long? = null,
        @Ignore
        val revenue: Long? = null,
        @Ignore
        val runtime: Long? = null,
        override val releaseDate: Date? = null,
        override val localeCode: String = SelectedLanguage.default.code,
        override val watchedDate: Date? = null,
        override val createdDate: Date? = null,
    ) : Item {

        constructor(
            id: Int,
            title: String,
            originalTitle: String,
            genreCodes: List<Int>,
            posterPath: String?,
            voteAverage: Float,
            releaseDate: Date?,
            localeCode: String,
            watchedDate: Date?,
            createdDate: Date?
        ) : this(
            id = id,
            title = title,
            originalTitle = originalTitle,
            genreCodes = genreCodes,
            posterPath = posterPath,
            voteAverage = voteAverage,
            releaseDate = releaseDate,
            localeCode = localeCode,
            watchedDate = watchedDate,
            createdDate = createdDate,
            overview = null
        )

        @Ignore
        val localization: SelectedLanguage = SelectedLanguage.from(localeCode)
        override val isWatched: Boolean
            get() = watchedDate != null
    }

    @Entity(tableName = "tv")
    data class Tv(
        @PrimaryKey
        override val id: Int,
        override val title: String,
        override val originalTitle: String,
        override val genreCodes: List<Int>,
        @Ignore
        override val overview: String? = null,
        override val posterPath: String?,
        override val voteAverage: Float,
        override val releaseDate: Date? = null,
        @Ignore
        val creator: String? = null,
        @Ignore
        val numberOfEpisodes: Int = 0,
        @Ignore
        val numberOfSeasons: Int = 0,
        @Ignore
        val lastAirDate: Date? = null,
        override val localeCode: String = SelectedLanguage.default.code,
        override val watchedDate: Date? = null,
        override val createdDate: Date? = null,
    ) : Item {

        constructor(
            id: Int,
            title: String,
            originalTitle: String,
            genreCodes: List<Int>,
            posterPath: String?,
            voteAverage: Float,
            releaseDate: Date?,
            localeCode: String,
            watchedDate: Date?,
            createdDate: Date?
        ) : this(
            id = id,
            title = title,
            originalTitle = originalTitle,
            genreCodes = genreCodes,
            posterPath = posterPath,
            voteAverage = voteAverage,
            releaseDate = releaseDate,
            localeCode = localeCode,
            watchedDate = watchedDate,
            createdDate = createdDate,
            overview = null
        )

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
            posterPath = null,
            voteAverage = 1f,
        )
    }
}
