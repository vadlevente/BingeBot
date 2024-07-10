package com.vadlevente.bingebot.core.data.local.db

import com.vadlevente.bingebot.core.data.dao.GenreDao
import com.vadlevente.bingebot.core.data.dao.MovieDao
import com.vadlevente.bingebot.core.data.dao.WatchListDao
import com.vadlevente.bingebot.core.model.Genre
import com.vadlevente.bingebot.core.model.Movie
import com.vadlevente.bingebot.core.model.WatchList
import java.util.Date
import javax.inject.Inject

class MovieLocalDataSource @Inject constructor(
    private val movieDao: MovieDao,
    private val genreDao: GenreDao,
    private val watchListDao: WatchListDao,
) {

    fun getAllMovies() = movieDao.getAllMovies()
    fun getAllMoviesWithIncorrectLocalization(locale: String) = movieDao.getAllIncorrectlyLocalizedMovies(locale)
    fun getAllGenres() = genreDao.getAllGenres()
    fun getAllWatchLists() = watchListDao.getAllWatchLists()
    fun getWatchList(watchListId: String) = watchListDao.getWatchList(watchListId)

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

    suspend fun deleteMovie(movieId: Int) {
        movieDao.deleteMovie(movieId)
    }

    suspend fun insertGenres(genres: List<Genre>) {
        genreDao.insertGenres(genres)
    }

    suspend fun deleteAllGenres() {
        genreDao.deleteAll()
    }

    suspend fun deleteAllWatchLists() {
        watchListDao.deleteAll()
    }

    suspend fun insertWatchList(watchList: WatchList) {
        watchListDao.insertWatchList(watchList)
    }

    suspend fun insertWatchLists(watchLists: List<WatchList>) {
        watchListDao.insertWatchLists(watchLists)
    }

    suspend fun setMovieWatchedDate(movieId: Int, watchedDate: Date?) {
        movieDao.setMovieWatchedDate(movieId, watchedDate)
    }

}