package com.vadlevente.bingebot.core.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey
    val id: Int,
    val title: String,
    val genres: List<Int>,
    val overview: String,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("vote_average")
    val voteAverage: Float,

)
