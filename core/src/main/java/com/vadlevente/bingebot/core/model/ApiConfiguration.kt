package com.vadlevente.bingebot.core.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import java.lang.Integer.min

@Entity(tableName = "configuration")
data class ApiConfiguration(
    @SerializedName("images")
    val imageConfiguration: ImageConfiguration
)

data class ImageConfiguration(
    @SerializedName("secure_base_url")
    val baseUrl: String,
    @SerializedName("poster_sizes")
    val posterSizes: List<String>,
) {
    val thumbnailSize: String
        get() = posterSizes[min(posterSizes.size-1, 3)]

    private val posterSize: String
        get() = posterSizes[posterSizes.size-1]

    val thumbnailBaseUrl: String
        get() = "$baseUrl$thumbnailSize"

    val posterBaseUrl: String
        get() = "$baseUrl$posterSize"

}
