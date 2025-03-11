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
    val job: Department
)

data class Creator(
    val name: String,
)

enum class Department(val code: String) {
    Director("Director"),
    Screenplay("Screenplay"),
    Writer("Writer"),
    Other("Other");

    companion object {
        fun ofValue(value: String) = Department.entries.firstOrNull { it.code == value } ?: Other
    }
}