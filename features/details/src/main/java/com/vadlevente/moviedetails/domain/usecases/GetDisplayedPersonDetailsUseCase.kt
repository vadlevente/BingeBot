package com.vadlevente.moviedetails.domain.usecases

import com.vadlevente.bingebot.core.model.PersonDetails
import com.vadlevente.bingebot.core.ui.stringOf
import com.vadlevente.bingebot.core.usecase.BaseUseCase
import com.vadlevente.bingebot.details.R
import com.vadlevente.moviedetails.domain.model.DisplayedCreditRow
import com.vadlevente.moviedetails.domain.model.DisplayedPersonDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

data class GetDisplayedPersonDetailsUseCaseParams(
    val details: PersonDetails,
    val castUpcomingHidden: Boolean,
    val castPreviousHidden: Boolean,
    val crewUpcomingHidden: Boolean,
    val crewPreviousHidden: Boolean,
)

class GetDisplayedPersonDetailsUseCase @Inject constructor(
) : BaseUseCase<GetDisplayedPersonDetailsUseCaseParams, DisplayedPersonDetails> {

    override fun execute(params: GetDisplayedPersonDetailsUseCaseParams): Flow<DisplayedPersonDetails> =
        flow {
            val details = params.details
            val rows = buildList<DisplayedCreditRow> {
                if (details.castCreditsUpcoming.isNotEmpty() || details.castCreditsPrevious.isNotEmpty()) {
                    add(
                        DisplayedCreditRow.DisplayedCreditHeader(
                            id = "castHeader",
                            title = stringOf(R.string.personDetails_castCredits)
                        )
                    )
                }
                if (details.castCreditsUpcoming.isNotEmpty()) {
                    add(
                        DisplayedCreditRow.DisplayedCreditSectionHeader(
                            id = "castUpcomingSectionHeader",
                            title = stringOf(
                                stringOf(R.string.personDetails_upcoming),
                                stringOf(" (${details.castCreditsUpcoming.size})")
                            ),
                            isCast = true,
                            isUpcoming = true,
                        )
                    )
                    if (!params.castUpcomingHidden) {
                        details.castCreditsUpcoming.forEach { credit ->
                            add(
                                DisplayedCreditRow.DisplayedCredit(
                                    id = credit.creditId,
                                    credit = credit,
                                )
                            )
                        }
                    }
                }
                if (details.castCreditsPrevious.isNotEmpty()) {
                    add(
                        DisplayedCreditRow.DisplayedCreditSectionHeader(
                            id = "castPreviousSectionHeader",
                            title = stringOf(
                                stringOf(R.string.personDetails_previous),
                                stringOf(" (${details.castCreditsPrevious.size})")
                            ),
                            isCast = true,
                            isUpcoming = false,
                        )
                    )
                    if (!params.castPreviousHidden) {
                        details.castCreditsPrevious.forEach { credit ->
                            add(
                                DisplayedCreditRow.DisplayedCredit(
                                    id = credit.creditId,
                                    credit = credit,
                                )
                            )
                        }
                    }
                }
                if (details.crewCreditsUpcoming.isNotEmpty() || details.crewCreditsPrevious.isNotEmpty()) {
                    add(
                        DisplayedCreditRow.DisplayedCreditHeader(
                            id = "crewHeader",
                            title = stringOf(R.string.personDetails_crewCredits)
                        )
                    )
                }
                if (details.crewCreditsUpcoming.isNotEmpty()) {
                    add(
                        DisplayedCreditRow.DisplayedCreditSectionHeader(
                            id = "crewUpcomingSectionHeader",
                            title = stringOf(
                                stringOf(R.string.personDetails_upcoming),
                                stringOf(" (${details.crewCreditsUpcoming.size})")
                            ),
                            isCast = false,
                            isUpcoming = true,
                        )
                    )
                    if (!params.crewUpcomingHidden) {
                        details.crewCreditsUpcoming.forEach { credit ->
                            add(
                                DisplayedCreditRow.DisplayedCredit(
                                    id = credit.creditId,
                                    credit = credit,
                                )
                            )
                        }
                    }
                }
                if (details.crewCreditsPrevious.isNotEmpty()) {
                    add(
                        DisplayedCreditRow.DisplayedCreditSectionHeader(
                            id = "crewPreviousSectionHeader",
                            title = stringOf(
                                stringOf(R.string.personDetails_previous),
                                stringOf(" (${details.crewCreditsPrevious.size})")
                            ),
                            isCast = false,
                            isUpcoming = false,
                        )
                    )
                    if (!params.crewPreviousHidden) {
                        details.crewCreditsPrevious.forEach { credit ->
                            add(
                                DisplayedCreditRow.DisplayedCredit(
                                    id = credit.creditId,
                                    credit = credit,
                                )
                            )
                        }
                    }
                }
            }
            emit(
                DisplayedPersonDetails(
                    name = details.name,
                    birthDay = details.birthDay,
                    deathDay = details.deathDay,
                    profileUrl = details.profileUrl,
                    rows = rows
                )
            )
        }

}