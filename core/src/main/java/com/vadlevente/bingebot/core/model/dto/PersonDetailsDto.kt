package com.vadlevente.bingebot.core.model.dto

import com.google.gson.annotations.SerializedName
import java.util.Date

data class PersonDetailsDto(
    val name: String,
    @SerializedName("birthday")
    val birthDay: Date?,
    @SerializedName("deathday")
    val deathDay: Date?,
    @SerializedName("profile_path")
    val profileUrl: String,
)
