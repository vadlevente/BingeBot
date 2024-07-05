package com.vadlevente.bingebot.core.data.remote

import com.vadlevente.bingebot.core.data.api.MovieApi
import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(
    private val movieApi: MovieApi,
) {

    suspend fun getConfiguration() = movieApi.fetchConfiguration()

}