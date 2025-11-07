package com.vadlevente.moviedetails.domain.model

import com.vadlevente.bingebot.core.UIText
import com.vadlevente.bingebot.core.model.PersonCredit
import java.util.Date

data class DisplayedPersonDetails(
    val name: String,
    val birthDay: Date?,
    val deathDay: Date?,
    val profileUrl: String?,
    val rows: List<DisplayedCreditRow>
)

sealed interface DisplayedCreditRow {
    val id: String
    data class DisplayedCreditHeader(
        override val id: String,
        val title: UIText
    ) : DisplayedCreditRow
    data class DisplayedCreditSectionHeader(
        override val id: String,
        val title: UIText,
        val isCast: Boolean,
        val isUpcoming: Boolean,
    ) : DisplayedCreditRow
    data class DisplayedCredit(
        override val id: String,
        val credit: PersonCredit
    ) : DisplayedCreditRow
}
