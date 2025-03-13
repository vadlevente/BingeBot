package com.vadlevente.moviedetails.domain.usecases

import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.data.repository.PersonRepository
import com.vadlevente.bingebot.core.model.PersonDetails
import com.vadlevente.bingebot.core.usecase.BaseUseCase
import com.vadlevente.bingebot.core.util.getPosterUrl
import com.vadlevente.bingebot.core.util.getProfileUrl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

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
            details.copy(
                profileUrl = details.getProfileUrl(configuration),
                castCredits = details.castCredits.map {
                    it.copy(
                        posterPath = it.getPosterUrl(configuration)
                    )
                },
                crewCredits = details.crewCredits.map {
                    it.copy(
                        posterPath = it.getPosterUrl(configuration)
                    )
                }
            )
        }
}