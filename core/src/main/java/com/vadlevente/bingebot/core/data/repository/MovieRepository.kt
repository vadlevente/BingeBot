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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.Date
import javax.inject.Inject

interface MovieRepository {
    fun getMovies(): Flow<List<Movie>>
    fun getGenres(): Flow<List<Genre>>
    fun getSearchResult(): Flow<List<Movie>>
    suspend fun updateConfiguration()
    suspend fun updateGenres()
    suspend fun updateMovies()
    suspend fun searchMovies(query: String)
    suspend fun updateMovieLocalizations()
    suspend fun saveMovie(movie: Movie)
    suspend fun addMovieToList(movie: Movie, watchList: WatchList)
    suspend fun saveMovieAndAddToWatchList(movie: Movie, watchList: WatchList)
}

class MovieRepositoryImpl @Inject constructor(
    private val movieRemoteDataSource: MovieRemoteDataSource,
    private val movieLocalDataSource: MovieLocalDataSource,
    private val movieCacheDataSource: MovieCacheDataSource,
    private val preferencesDataSource: PreferencesDataSource,
    private val firestoreDataSource: FirestoreDataSource,
) : MovieRepository {

    override fun getMovies(): Flow<List<Movie>> = movieLocalDataSource.getAllMovies()

    override fun getGenres(): Flow<List<Genre>> = movieLocalDataSource.getAllGenres()

    override fun getSearchResult(): Flow<List<Movie>> = movieCacheDataSource.moviesState

    override suspend fun updateConfiguration() {
        val config = movieRemoteDataSource.getConfiguration()
        preferencesDataSource.saveApiConfiguration(config)
    }

    override suspend fun updateGenres() {
        val language = preferencesDataSource.language.first()
        val genres = movieRemoteDataSource.getGenres(language)
        movieLocalDataSource.deleteAllGenres()
        movieLocalDataSource.insertGenres(genres.genres)
    }

    override suspend fun updateMovies() {
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

    override suspend fun searchMovies(query: String) {
        val language = preferencesDataSource.language.first()
        val movies = movieRemoteDataSource.searchMovie(query, language)
        movieCacheDataSource.updateMovies(movies, language)
    }

    override suspend fun updateMovieLocalizations() {
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

    override suspend fun saveMovie(movie: Movie) {
        val createdDate = Date()
        firestoreDataSource.addMovie(
            StoredMovie(
                id = movie.id.toString(),
                createdDate = createdDate,
            )
        )
        movieLocalDataSource.insertMovie(movie.copy(createdDate = createdDate))
    }

    override suspend fun saveMovieAndAddToWatchList(movie: Movie, watchList: WatchList) {
//        firestoreDataSource.addMovie(StoredMovie(
//            id = movie.id.toString(),
//            createdDate = movie.createdDate,
//        )
//        )
//        firestoreDataSource.addMovieToWatchList(watchList.watchListId, movie.id)
//        movieLocalDataSource.insertMovie(movie)

    }

    override suspend fun addMovieToList(movie: Movie, watchList: WatchList) {
//        firestoreDataSource.addMovieToWatchList(watchList.watchListId, movie.id)
    }


}