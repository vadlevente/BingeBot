package com.vadlevente.bingebot.core.data.repository

import com.vadlevente.bingebot.core.data.local.db.MovieLocalDataSource
import com.vadlevente.bingebot.core.data.remote.MovieRemoteDataSource
import javax.inject.Inject

interface MovieRepository {
    suspend fun updateConfiguration()
}

class MovieRepositoryImpl @Inject constructor(
    private val movieRemoteDataSource: MovieRemoteDataSource,
    private val movieLocalDataSource: MovieLocalDataSource,
) : MovieRepository {

    override suspend fun updateConfiguration() {
        val config = movieRemoteDataSource.getConfiguration()
        movieLocalDataSource.updateConfiguration(config)
    }

}