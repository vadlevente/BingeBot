package com.vadlevente.moviedetails.domain.usecases

import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.data.repository.PersonRepository
import com.vadlevente.bingebot.core.model.ApiConfiguration
import com.vadlevente.bingebot.core.model.PersonDetails
import com.vadlevente.bingebot.core.usecase.BaseUseCase
import com.vadlevente.bingebot.core.util.getPosterUrl
import com.vadlevente.bingebot.core.util.getProfileUrl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.map

data class GetPersonDetailsUseCaseParams(
    val personId: Int,
)

class GetPersonDetailsUseCase @Inject constructor(
    private val personRepository: PersonRepository,
    private val preferencesDataSource: PreferencesDataSource,
) : BaseUseCase<GetPersonDetailsUseCaseParams, PersonDetails> {

    override fun execute(params: GetPersonDetailsUseCaseParams): Flow<PersonDetails> =
        combine(
            preferencesDataSource.apiConfiguration,
            personRepository.getPersonDetails(params.personId),
            ::Pair
        ).map { (configuration, details) ->
            details.getImageUrls(configuration)
        }

    private fun PersonDetails.getImageUrls(configuration: ApiConfiguration) =
        copy(
            profileUrl = getProfileUrl(configuration),
            castCreditsUpcoming = castCreditsUpcoming.map {
                it.copy(
                    posterPath = it.getPosterUrl(configuration)
                )
            },
            castCreditsPrevious = castCreditsPrevious.map {
                it.copy(
                    posterPath = it.getPosterUrl(configuration)
                )
            },
            crewCreditsUpcoming = crewCreditsUpcoming.map {
                it.copy(
                    posterPath = it.getPosterUrl(configuration)
                )
            },
            crewCreditsPrevious = crewCreditsPrevious.map {
                it.copy(
                    posterPath = it.getPosterUrl(configuration)
                )
            }
        )
}