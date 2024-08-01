package com.vadlevente.bingebot.core.data.local.db

import com.vadlevente.bingebot.core.data.dao.GenreDao
import com.vadlevente.bingebot.core.data.dao.TvDao
import com.vadlevente.bingebot.core.data.dao.WatchListDao
import com.vadlevente.bingebot.core.model.Genre
import com.vadlevente.bingebot.core.model.Item.Tv
import com.vadlevente.bingebot.core.model.ItemType.TV
import com.vadlevente.bingebot.core.model.WatchList
import javax.inject.Inject

class TvLocalDataSource @Inject constructor(
    tvDao: TvDao,
    private val genreDao: GenreDao,
    private val watchListDao: WatchListDao,
) : ItemLocalDataSource<Tv>(
    tvDao, watchListDao
) {
    override fun getAllWatchLists() = watchListDao.getAllWatchLists(TV)

    override suspend fun deleteAllWatchLists() {
        watchListDao.deleteAll(TV)
    }

    override suspend fun insertWatchList(watchList: WatchList) {
        watchListDao.insertWatchList(watchList.copy(type = TV))
    }

    override suspend fun insertWatchLists(watchLists: List<WatchList>) {
        watchListDao.insertWatchLists(
            watchLists.map {
                it.copy(type = TV)
            }
        )
    }

    override fun getAllGenres() = genreDao.getAllGenres(TV)

    override suspend fun insertGenres(genres: List<Genre>) {
        genreDao.insertGenres(
            genres.map {
                it.copy(type = TV)
            }
        )
    }

    override suspend fun deleteAllGenres() {
        genreDao.deleteAll(TV)
    }


}