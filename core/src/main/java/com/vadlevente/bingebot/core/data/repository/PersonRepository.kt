package com.vadlevente.bingebot.core.data.repository

import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.data.remote.PersonRemoteDataSource
import com.vadlevente.bingebot.core.model.MediaType
import com.vadlevente.bingebot.core.model.PersonCredit
import com.vadlevente.bingebot.core.model.PersonDetails
import kotlinx.coroutines.flow.Flow
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
        preferencesDataSource.language.flatMapConcat { language ->
            val details = personRemoteDataSource.getDetails(personId, language)
            val credits = personRemoteDataSource.getCombinedCredits(personId, language)
            PersonDetails(
                name = details.name,
                birthDay = details.birthDay,
                deathDay = details.deathDay,
                profileUrl = details.profileUrl,
                castCredits = credits.castCredits.filter {
                    !it.title.isNullOrEmpty() &&
                        it.releaseDate != null
                }.map { credit ->
                    PersonCredit(
                        id = credit.id,
                        title = credit.title!!,
                        posterPath = credit.posterPath,
                        releaseDate = credit.releaseDate!!,
                        voteAverage = credit.voteAverage,
                        info = credit.character,
                        mediaType = MediaType.from(credit.mediaType)
                    )
                }.sortedByDescending { it.releaseDate },
                crewCredits = credits.crewCredits.filter {
                    !it.title.isNullOrEmpty() &&
                        it.releaseDate != null
                }.map { credit ->
                    PersonCredit(
                        id = credit.id,
                        title = credit.title!!,
                        posterPath = credit.posterPath,
                        releaseDate = credit.releaseDate!!,
                        voteAverage = credit.voteAverage,
                        info = credit.job,
                        mediaType = MediaType.from(credit.mediaType)
                    )
                }.sortedByDescending { it.releaseDate },
            ).let { flowOf(it) }
        }
}