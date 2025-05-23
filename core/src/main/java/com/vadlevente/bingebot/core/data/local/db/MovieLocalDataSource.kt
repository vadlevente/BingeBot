package com.vadlevente.bingebot.core.data.local.db

import com.vadlevente.bingebot.core.data.dao.GenreDao
import com.vadlevente.bingebot.core.data.dao.MovieDao
import com.vadlevente.bingebot.core.data.dao.WatchListDao
import com.vadlevente.bingebot.core.model.Genre
import com.vadlevente.bingebot.core.model.Item.Movie
import com.vadlevente.bingebot.core.model.ItemType.MOVIE
import com.vadlevente.bingebot.core.model.WatchList
import javax.inject.Inject

class MovieLocalDataSource @Inject constructor(
    movieDao: MovieDao,
    private val genreDao: GenreDao,
    private val watchListDao: WatchListDao,
) : ItemLocalDataSource<Movie>(
    movieDao, watchListDao
) {
    override fun getAllWatchLists() = watchListDao.getAllWatchLists(MOVIE)

    override suspend fun deleteAllWatchLists() {
        watchListDao.deleteAll(MOVIE)
    }

    override suspend fun insertWatchList(watchList: WatchList) {
        watchListDao.insertWatchList(watchList.copy(type = MOVIE))
    }

    override suspend fun insertWatchLists(watchLists: List<WatchList>) {
        watchListDao.insertWatchLists(
            watchLists.map {
                it.copy(type = MOVIE)
            }
        )
    }

    override fun getAllGenres() = genreDao.getAllGenres(MOVIE)

    override suspend fun insertGenres(genres: List<Genre>) {
        genreDao.insertGenres(
            genres.map {
                it.copy(type = MOVIE)
            }
        )
    }

    override suspend fun deleteAllGenres() {
        genreDao.deleteAll(MOVIE)
    }


}