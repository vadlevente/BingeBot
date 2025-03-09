package com.vadlevente.bingebot.core.model

import com.google.gson.annotations.SerializedName

data class Credits(
    val cast: List<CastMember>,
    val director: List<CrewMember>,
    val writer: List<CrewMember>,
)

data class CastMember(
    val id: Long,
    val name: String,
    @SerializedName("profile_path")
    val profileUrl: String,
    val character: String,
)

data class CrewMember(
    val id: Long,
    val name: String,
    val department: Department
)

enum class Department(val code: String) {
    Director("Director"),
    Screenplay("Screenplay"),
    Other("Other");

    companion object {
        fun ofValue(value: String) = values().firstOrNull { it.code == value } ?: Other
    }
}