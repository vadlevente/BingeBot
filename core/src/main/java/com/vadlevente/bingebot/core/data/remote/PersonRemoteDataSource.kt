package com.vadlevente.bingebot.core.data.remote

import com.vadlevente.bingebot.core.data.api.PersonApi
import com.vadlevente.bingebot.core.model.SelectedLanguage
import javax.inject.Inject

class PersonRemoteDataSource @Inject constructor(
    private val personApi: PersonApi,
) {

    suspend fun getDetails(
        personId: Int,
        language: SelectedLanguage,
    ) = personApi.fetchDetails(personId, language.code)

    suspend fun getCombinedCredits(
        personId: Int,
        language: SelectedLanguage,
    ) = personApi.fetchCombinedCredits(personId, language.code)

}