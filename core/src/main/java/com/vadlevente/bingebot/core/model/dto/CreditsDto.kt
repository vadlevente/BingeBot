package com.vadlevente.bingebot.core.model.dto

import com.vadlevente.bingebot.core.model.CastMember
import com.vadlevente.bingebot.core.model.CrewMember

data class CreditsDto(
    val castMembers: List<CastMember>,
    val crewMembers: List<CrewMember>,
)
