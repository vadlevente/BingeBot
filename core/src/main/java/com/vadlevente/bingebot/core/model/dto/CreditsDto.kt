package com.vadlevente.bingebot.core.model.dto

import com.google.gson.annotations.SerializedName
import com.vadlevente.bingebot.core.model.CastMember
import com.vadlevente.bingebot.core.model.CrewMember

data class CreditsDto(
    @SerializedName("cast")
    val castMembers: List<CastMember>,
    @SerializedName("crew")
    val crewMembers: List<CrewMember>,
)
