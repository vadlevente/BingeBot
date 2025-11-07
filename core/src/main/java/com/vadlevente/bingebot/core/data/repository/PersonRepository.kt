package com.vadlevente.bingebot.core.data.repository

import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.data.remote.PersonRemoteDataSource
import com.vadlevente.bingebot.core.model.MediaType
import com.vadlevente.bingebot.core.model.PersonCredit
import com.vadlevente.bingebot.core.model.PersonDetails
import com.vadlevente.bingebot.core.model.dto.CastCredit
import com.vadlevente.bingebot.core.model.dto.CrewCredit
import com.vadlevente.bingebot.core.util.asDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

interface PersonRepository {
    fun getPersonDetails(personId: Int): Flow<PersonDetails>
}

class PersonRepositoryImpl @Inject constructor(
    private val personRemoteDataSource: PersonRemoteDataSource,
    private val preferencesDataSource: PreferencesDataSource,
) : PersonRepository {
    override fun getPersonDetails(personId: Int) =
        preferencesDataSource.language.filterNotNull().flatMapConcat { language ->
            val details = personRemoteDataSource.getDetails(personId, language)
            val credits = personRemoteDataSource.getCombinedCredits(personId, language)
            val castCredits = getCastCredits(credits.castCredits)
            val crewCredits = getCrewCredits(credits.crewCredits)
            PersonDetails(
                name = details.name,
                birthDay = details.birthDay,
                deathDay = details.deathDay,
                profileUrl = details.profileUrl,
                castCreditsUpcoming = castCredits.filter { it.isFutureRelease },
                castCreditsPrevious = castCredits.filter { !it.isFutureRelease }.sortedByDescending { it.releaseDate },
                crewCreditsUpcoming = crewCredits.filter { it.isFutureRelease },
                crewCreditsPrevious = crewCredits.filter { !it.isFutureRelease }.sortedByDescending { it.releaseDate },
            ).let { flowOf(it) }
        }

    private fun getCastCredits(credits: List<CastCredit>) = credits.filter {
        !it.title.isNullOrEmpty() || !it.name.isNullOrEmpty()
    }.map { credit ->
        val date = credit.releaseDate ?: credit.firstCreditAirDate?.asDate
        val title = credit.title ?: credit.name
        PersonCredit(
            id = credit.id.toInt(),
            creditId = credit.creditId,
            title = title ?: "",
            posterPath = credit.posterPath,
            releaseDate = date,
            voteAverage = credit.voteAverage,
            info = credit.character,
            mediaType = MediaType.from(credit.mediaType),
            isFutureRelease = date == null,
        )
    }

    private fun getCrewCredits(credits: List<CrewCredit>) = credits.filter {
        !it.title.isNullOrEmpty() || !it.name.isNullOrEmpty()
    }.map { credit ->
        val date = credit.releaseDate ?: credit.firstCreditAirDate?.asDate
        val title = credit.title ?: credit.name
        PersonCredit(
            id = credit.id.toInt(),
            creditId = credit.creditId,
            title = title ?: "",
            posterPath = credit.posterPath,
            releaseDate = date,
            voteAverage = credit.voteAverage,
            info = credit.job,
            mediaType = MediaType.from(credit.mediaType),
            isFutureRelease = date == null,
        )
    }
}