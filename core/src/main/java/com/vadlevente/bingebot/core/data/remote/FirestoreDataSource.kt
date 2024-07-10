package com.vadlevente.bingebot.core.data.remote

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.model.WatchList
import com.vadlevente.bingebot.core.model.exception.BingeBotException
import com.vadlevente.bingebot.core.model.exception.Reason.DATA_READ_ERROR
import com.vadlevente.bingebot.core.model.exception.Reason.DATA_WRITE_ERROR
import com.vadlevente.bingebot.core.model.exception.Reason.SESSION_EXPIRED
import com.vadlevente.bingebot.core.model.exception.Reason.WATCHLIST_ALREADY_EXISTS
import com.vadlevente.bingebot.core.model.firestore.StoredMovie
import com.vadlevente.bingebot.core.model.firestore.StoredMovies
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirestoreDataSource @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource,
    private val firestore: FirebaseFirestore,
) {

    suspend fun getMovies(): Flow<List<StoredMovie>> {
        return flow {
            val profileId = preferencesDataSource.activeProfileId.first() ?: return@flow
            val movies = suspendCoroutine { continuation ->
                firestore.collection(COLLECTION_MOVIES)
                    .document(profileId)
                    .collection(COLLECTION_MOVIES)
                    .get()
                    .addOnSuccessListener { result ->
                        val movies = result.map {
                            it.toObject<StoredMovie>()
                        }
                        continuation.resume(movies)
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(BingeBotException(it, DATA_READ_ERROR))
                    }
            }
            emit(movies)
        }
    }

    suspend fun getWatchLists(): Flow<List<WatchList>> {
        return flow {
            val profileId = preferencesDataSource.activeProfileId.first() ?: return@flow
            val watchLists = suspendCoroutine { continuation ->
                firestore.collection(COLLECTION_MOVIES)
                    .document(profileId)
                    .collection(COLLECTION_WATCHLISTS)
                    .get()
                    .addOnSuccessListener { result ->
                        val watchLists = result.map {
                            it.toObject<WatchList>()
                        }
                        continuation.resume(watchLists)
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(BingeBotException(it, DATA_READ_ERROR))
                    }
            }
            emit(watchLists)
        }
    }

    suspend fun createUser(userId: String) = suspendCoroutine { continuation ->
        firestore.collection(COLLECTION_MOVIES)
            .document(userId)
            .set(StoredMovies(emptyList(), emptyList()))
            .addOnSuccessListener {
                continuation.resume(Unit)
            }
            .addOnFailureListener {
                continuation.resumeWithException(BingeBotException(it, DATA_WRITE_ERROR))
            }
    }

    suspend fun addMovie(movie: StoredMovie) {
        val profileId = preferencesDataSource.activeProfileId.first() ?: return
        suspendCoroutine { continuation ->
            firestore.collection(COLLECTION_MOVIES)
                .document(profileId)
                .collection(COLLECTION_MOVIES)
                .document(movie.id)
                .set(movie)
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(BingeBotException(it, DATA_WRITE_ERROR))
                }
        }
    }

    suspend fun deleteMovie(movieId: Int) {
        val profileId = preferencesDataSource.activeProfileId.first() ?: return
        suspendCoroutine { continuation ->
            firestore.collection(COLLECTION_MOVIES)
                .document(profileId)
                .collection(COLLECTION_MOVIES)
                .document(movieId.toString())
                .delete()
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(BingeBotException(it, DATA_WRITE_ERROR))
                }
        }
        getWatchLists().first().filter {
            it.movieIds.contains(movieId)
        }.forEach {
            removeMovieFromWatchList(it.watchListId, movieId)
        }
    }

    suspend fun setMovieWatchDate(movieId: Int, watchDate: Date?) {
        val profileId = preferencesDataSource.activeProfileId.first() ?: return
        suspendCoroutine { continuation ->
            firestore.collection(COLLECTION_MOVIES)
                .document(profileId)
                .collection(COLLECTION_MOVIES)
                .document("$movieId")
                .update(FIELD_WATCHDATE, watchDate)
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(BingeBotException(it, DATA_WRITE_ERROR))
                }
        }
    }

    suspend fun addWatchList(title: String): String {
        val watchLists = getWatchLists().first()
        if (watchLists.any { it.title == title }) {
            throw BingeBotException(reason = WATCHLIST_ALREADY_EXISTS)
        }
        val profileId = preferencesDataSource.activeProfileId.first() ?: throw BingeBotException(
            reason = SESSION_EXPIRED
        )
        val watchListId = UUID.randomUUID().toString()
        return suspendCoroutine { continuation ->
            firestore.collection(COLLECTION_MOVIES)
                .document(profileId)
                .collection(COLLECTION_WATCHLISTS)
                .document(watchListId)
                .set(
                    WatchList(
                        watchListId = watchListId,
                        title = title,
                        movieIds = emptyList(),
                    )
                )
                .addOnSuccessListener {
                    continuation.resume(watchListId)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(BingeBotException(it, DATA_WRITE_ERROR))
                }
        }
    }

    suspend fun addMovieToWatchList(watchListId: String, movieId: Int) {
        val profileId = preferencesDataSource.activeProfileId.first() ?: return
        suspendCoroutine { continuation ->
            firestore.collection(COLLECTION_MOVIES)
                .document(profileId)
                .collection(COLLECTION_WATCHLISTS)
                .document(watchListId)
                .update(FIELD_MOVIE_IDS, FieldValue.arrayUnion(movieId))
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(BingeBotException(it, DATA_WRITE_ERROR))
                }
        }
    }

    suspend fun removeMovieFromWatchList(watchListId: String, movieId: Int) {
        val profileId = preferencesDataSource.activeProfileId.first() ?: return
        suspendCoroutine { continuation ->
            firestore.collection(COLLECTION_MOVIES)
                .document(profileId)
                .collection(COLLECTION_WATCHLISTS)
                .document(watchListId)
                .update(FIELD_MOVIE_IDS, FieldValue.arrayRemove(movieId))
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(BingeBotException(it, DATA_WRITE_ERROR))
                }
        }
    }


    companion object {
        const val COLLECTION_MOVIES = "movies"
        const val COLLECTION_WATCHLISTS = "watchLists"
        const val FIELD_MOVIE_ID = "movieId"
        const val FIELD_WATCHDATE = "watchDate"
        const val FIELD_MOVIE_IDS = "movieIds"
        const val FIELD_TITLE = "title"

    }

}