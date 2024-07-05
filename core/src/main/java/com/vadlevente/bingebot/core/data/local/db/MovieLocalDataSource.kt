package com.vadlevente.bingebot.core.data.local.db

import com.vadlevente.bingebot.core.data.dao.MovieDao
import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.model.ApiConfiguration
import com.vadlevente.bingebot.core.model.Movie
import javax.inject.Inject

class MovieLocalDataSource @Inject constructor(
    private val movieDao: MovieDao,
    private val preferencesDataSource: PreferencesDataSource,
) {
    suspend fun updateConfiguration(apiConfiguration: ApiConfiguration) {
        preferencesDataSource.saveApiConfiguration(apiConfiguration)
    }

    fun getAllMovies() = movieDao.getAllMovies()

    suspend fun updateMovie(movie: Movie) {
        movieDao.insertMovie(movie)
    }

    suspend fun insertMovie(movie: Movie) {
        movieDao.insertMovie(movie)
    }

    suspend fun deleteMovie(movie: Movie) {
        movieDao.deleteMovie(movie)
    }

}