package com.vadlevente.bingebot.core.data.repository

import com.vadlevente.bingebot.core.data.cache.MovieCacheDataSource
import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.data.local.db.MovieLocalDataSource
import com.vadlevente.bingebot.core.data.remote.FirestoreDataSource
import com.vadlevente.bingebot.core.data.remote.MovieRemoteDataSource
import com.vadlevente.bingebot.core.model.Genre
import com.vadlevente.bingebot.core.model.Movie
import com.vadlevente.bingebot.core.model.WatchList
import com.vadlevente.bingebot.core.model.firestore.StoredMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

interface MovieRepository {
    fun getMovies(): Flow<List<Movie>>
    fun getGenres(): Flow<List<Genre>>
    fun getSearchResult(): Flow<List<Movie>>
    fun getWatchLists(): Flow<List<WatchList>>
    fun getWatchListMovies(watchListId: String): Flow<List<Movie>>
    suspend fun updateConfiguration()
    suspend fun updateGenres()
    suspend fun updateMovies()
    suspend fun updateWatchLists()
    suspend fun searchMovies(query: String)
    suspend fun updateMovieLocalizations()
    suspend fun saveMovie(movie: Movie)
    suspend fun deleteMovie(movieId: Int)
    suspend fun createWatchList(title: String): String
    suspend fun addMovieToList(movieId: Int, watchListId: String): Boolean
    suspend fun removeMovieFromList(movieId: Int, watchListId: String)
    suspend fun setMovieWatchedDate(movieId: Int, watchedDate: Date?)
    suspend fun deleteWatchList(watchListId: String)
}

class MovieRepositoryImpl @Inject constructor(
    private val movieRemoteDataSource: MovieRemoteDataSource,
    private val movieLocalDataSource: MovieLocalDataSource,
    private val movieCacheDataSource: MovieCacheDataSource,
    private val preferencesDataSource: PreferencesDataSource,
    private val firestoreDataSource: FirestoreDataSource,
) : MovieRepository {

    override fun getMovies(): Flow<List<Movie>> =
        movieLocalDataSource.getAllMovies().flowOn(Dispatchers.IO)

    override fun getGenres(): Flow<List<Genre>> =
        movieLocalDataSource.getAllGenres().flowOn(Dispatchers.IO)

    override fun getSearchResult(): Flow<List<Movie>> = movieCacheDataSource.moviesState

    override fun getWatchLists(): Flow<List<WatchList>> =
        movieLocalDataSource.getAllWatchLists().flowOn(Dispatchers.IO)

    override fun getWatchListMovies(watchListId: String): Flow<List<Movie>> =
        movieLocalDataSource.getWatchList(watchListId)
            .flatMapConcat { watchList ->
                movieLocalDataSource.getMovies(watchList.movieIds)
            }.flowOn(Dispatchers.IO)

    override suspend fun updateConfiguration() = withContext(Dispatchers.IO) {
        val config = movieRemoteDataSource.getConfiguration()
        preferencesDataSource.saveApiConfiguration(config)
    }

    override suspend fun updateGenres() = withContext(Dispatchers.IO) {
        val language = preferencesDataSource.language.first()
        val genres = movieRemoteDataSource.getGenres(language)
        movieLocalDataSource.deleteAllGenres()
        movieLocalDataSource.insertGenres(genres.genres)
    }

    override suspend fun updateMovies() = withContext(Dispatchers.IO) {
        val language = preferencesDataSource.language.first()
        val storedMovies = movieLocalDataSource.getAllMovies().first()
        val remoteMovies = firestoreDataSource.getMovies().first()
        val moviesToUpdate = storedMovies.filter { storedMovie ->
            storedMovie.watchedDate != remoteMovies.first { it.id.toInt() == storedMovie.id }.watchDate
        }.map { storedMovie ->
            storedMovie.copy(watchedDate = remoteMovies.first { it.id.toInt() == storedMovie.id }.watchDate)
        }
        val moviesToInsert = remoteMovies
            .map { it.id.toInt() }
            .minus(storedMovies.map { it.id }
                .toSet())
            .map { id ->
                movieRemoteDataSource.getMovieDetails(id, language).toMovie().copy(
                    localeCode = language.code,
                    createdDate = remoteMovies.first { it.id.toInt() == id }.createdDate
                )
            }
        movieLocalDataSource.updateMovies(moviesToUpdate.plus(moviesToInsert))
    }

    override suspend fun updateWatchLists() = withContext(Dispatchers.IO) {
        movieLocalDataSource.deleteAllWatchLists()
        val remoteWatchLists = firestoreDataSource.getWatchLists().first()
        movieLocalDataSource.insertWatchLists(remoteWatchLists)
    }

    override suspend fun searchMovies(query: String) = withContext(Dispatchers.IO) {
        val language = preferencesDataSource.language.first()
        val movies = movieRemoteDataSource.searchMovie(query, language)
        movieCacheDataSource.updateMovies(movies, language)
    }

    override suspend fun updateMovieLocalizations() = withContext(Dispatchers.IO) {
        val language = preferencesDataSource.language.first()
        val storedMoviesWithIncorrectLocalization =
            movieLocalDataSource.getAllMoviesWithIncorrectLocalization(language.code).first()
        storedMoviesWithIncorrectLocalization.map {
            movieRemoteDataSource.getMovieDetails(it.id, language).toMovie().copy(
                localeCode = language.code
            )
        }.let {
            movieLocalDataSource.updateMovies(it)
        }
    }

    override suspend fun saveMovie(movie: Movie) = withContext(Dispatchers.IO) {
        val createdDate = Date()
        firestoreDataSource.addMovie(
            StoredMovie(
                id = movie.id.toString(),
                createdDate = createdDate,
            )
        )
        movieLocalDataSource.insertMovie(movie.copy(createdDate = createdDate))
    }

    override suspend fun deleteMovie(movieId: Int) = withContext(Dispatchers.IO) {
        firestoreDataSource.deleteMovie(movieId)
        movieLocalDataSource.deleteMovie(movieId)
        movieLocalDataSource.getAllWatchLists().first()
            .filter { it.movieIds.contains(movieId) }
            .forEach { watchList ->
                movieLocalDataSource.insertWatchList(
                    watchList.copy(
                        movieIds = watchList.movieIds.minus(movieId)
                    )
                )
            }
    }

    override suspend fun createWatchList(title: String): String =
        withContext(Dispatchers.IO) {
            val watchListId = firestoreDataSource.addWatchList(title)
            movieLocalDataSource.insertWatchList(
                WatchList(
                    watchListId = watchListId,
                    title = title,
                    movieIds = emptyList(),
                )
            )
            return@withContext watchListId
        }

    override suspend fun addMovieToList(movieId: Int, watchListId: String) =
        withContext(Dispatchers.IO) {
            val watchList = movieLocalDataSource.getWatchList(watchListId).first()
            if (watchList.movieIds.contains(movieId)) {
                return@withContext false
            }
            movieLocalDataSource.insertWatchList(
                watchList.copy(
                    movieIds = watchList.movieIds.plus(movieId)
                )
            )
            firestoreDataSource.addMovieToWatchList(watchListId, movieId)
            return@withContext true
        }

    override suspend fun removeMovieFromList(movieId: Int, watchListId: String) =
        withContext(Dispatchers.IO) {
            firestoreDataSource.removeMovieFromWatchList(watchListId, movieId)
            val watchList = movieLocalDataSource.getWatchList(watchListId).first()
            movieLocalDataSource.insertWatchList(
                watchList.copy(
                    movieIds = watchList.movieIds.minus(movieId)
                )
            )
        }

    override suspend fun setMovieWatchedDate(movieId: Int, watchedDate: Date?) =
        withContext(Dispatchers.IO) {
            firestoreDataSource.setMovieWatchDate(movieId, watchedDate)
            movieLocalDataSource.setMovieWatchedDate(movieId, watchedDate)
        }

    override suspend fun deleteWatchList(watchListId: String) = withContext(Dispatchers.IO) {
        firestoreDataSource.deleteWatchList(watchListId)
        movieLocalDataSource.deleteWatchList(watchListId)
    }

}