package com.vadlevente.bingebot.core.data.local.db

import com.vadlevente.bingebot.core.data.dao.GenreDao
import com.vadlevente.bingebot.core.data.dao.MovieDao
import com.vadlevente.bingebot.core.model.Genre
import com.vadlevente.bingebot.core.model.Movie
import javax.inject.Inject

class MovieLocalDataSource @Inject constructor(
    private val movieDao: MovieDao,
    private val genreDao: GenreDao,
) {

    fun getAllMovies() = movieDao.getAllMovies()
    fun getAllMoviesWithIncorrectLocalization(locale: String) = movieDao.getAllIncorrectlyLocalizedMovies(locale)
    fun getAllGenres() = genreDao.getAllGenres()

    suspend fun updateMovie(movie: Movie) {
        movieDao.insertMovie(movie)
    }

    suspend fun updateMovies(movies: List<Movie>) {
        movieDao.insertMovies(movies)
    }

    suspend fun insertMovie(movie: Movie) {
        movieDao.insertMovie(movie)
    }

    suspend fun deleteMovie(movie: Movie) {
        movieDao.deleteMovie(movie)
    }

    suspend fun insertGenres(genres: List<Genre>) {
        genreDao.insertGenres(genres)
    }

    suspend fun deleteAllGenres() {
        genreDao.deleteAll()
    }

}