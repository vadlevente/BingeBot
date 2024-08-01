package com.vadlevente.bingebot.core.data.remote.firestore

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.model.WatchList
import com.vadlevente.bingebot.core.model.WatchListFactory
import com.vadlevente.bingebot.core.model.config.FirebaseCollectionPaths
import com.vadlevente.bingebot.core.model.exception.BingeBotException
import com.vadlevente.bingebot.core.model.exception.Reason.DATA_READ_ERROR
import com.vadlevente.bingebot.core.model.exception.Reason.DATA_WRITE_ERROR
import com.vadlevente.bingebot.core.model.exception.Reason.SESSION_EXPIRED
import com.vadlevente.bingebot.core.model.exception.Reason.WATCHLIST_ALREADY_EXISTS
import com.vadlevente.bingebot.core.model.firestore.StoredItem
import com.vadlevente.bingebot.core.util.Constants.FIREBASE_COLLECTION_ROOT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface FirestoreItemDataSource <T : Item> {
    suspend fun getItems(): Flow<List<StoredItem>>
    suspend fun getWatchLists(): Flow<List<WatchList>>
    suspend fun addItem(item: StoredItem)
    suspend fun deleteItem(itemId: Int)
    suspend fun setItemWatchDate(itemId: Int, watchDate: Date?)
    suspend fun addWatchList(title: String): String
    suspend fun addItemToWatchList(watchListId: String, itemId: Int)
    suspend fun removeItemFromWatchList(watchListId: String, itemId: Int)
    suspend fun deleteWatchList(watchListId: String)
}

class FirestoreItemDataSourceImpl <T : Item> @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource,
    private val firestore: FirebaseFirestore,
    private val firebaseCollectionPaths: FirebaseCollectionPaths<T>,
    private val watchListFactory: WatchListFactory<T>,
) : FirestoreItemDataSource<T> {
    
    override suspend fun getItems(): Flow<List<StoredItem>> {
        return flow {
            val profileId = preferencesDataSource.activeProfileId.first() ?: return@flow
            val items = suspendCoroutine { continuation ->
                firestore.collection(FIREBASE_COLLECTION_ROOT)
                    .document(profileId)
                    .collection(firebaseCollectionPaths.ITEM_COLLECTION_PATH)
                    .get()
                    .addOnSuccessListener { result ->
                        val movies = result.map {
                            it.toObject<StoredItem>()
                        }
                        continuation.resume(movies)
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(BingeBotException(it, DATA_READ_ERROR))
                    }
            }
            emit(items)
        }
    }

    override suspend fun getWatchLists(): Flow<List<WatchList>> {
        return flow {
            val profileId = preferencesDataSource.activeProfileId.first() ?: return@flow
            val watchLists = suspendCoroutine { continuation ->
                firestore.collection(FIREBASE_COLLECTION_ROOT)
                    .document(profileId)
                    .collection(firebaseCollectionPaths.ITEM_WATCHLIST_PATH)
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

    override suspend fun addItem(item: StoredItem) {
        val profileId = preferencesDataSource.activeProfileId.first() ?: return
        suspendCoroutine { continuation ->
            firestore.collection(FIREBASE_COLLECTION_ROOT)
                .document(profileId)
                .collection(firebaseCollectionPaths.ITEM_COLLECTION_PATH)
                .document(item.id)
                .set(item)
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(BingeBotException(it, DATA_WRITE_ERROR))
                }
        }
    }

    override suspend fun deleteItem(itemId: Int) {
        val profileId = preferencesDataSource.activeProfileId.first() ?: return
        suspendCoroutine { continuation ->
            firestore.collection(FIREBASE_COLLECTION_ROOT)
                .document(profileId)
                .collection(firebaseCollectionPaths.ITEM_COLLECTION_PATH)
                .document(itemId.toString())
                .delete()
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(BingeBotException(it, DATA_WRITE_ERROR))
                }
        }
        getWatchLists().first().filter {
            it.itemIds.contains(itemId)
        }.forEach {
            removeItemFromWatchList(it.watchListId, itemId)
        }
    }

    override suspend fun setItemWatchDate(itemId: Int, watchDate: Date?) {
        val profileId = preferencesDataSource.activeProfileId.first() ?: return
        suspendCoroutine { continuation ->
            firestore.collection(FIREBASE_COLLECTION_ROOT)
                .document(profileId)
                .collection(firebaseCollectionPaths.ITEM_COLLECTION_PATH)
                .document("$itemId")
                .update(FIELD_WATCHDATE, watchDate)
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(BingeBotException(it, DATA_WRITE_ERROR))
                }
        }
    }

    override suspend fun addWatchList(title: String): String {
        val watchLists = getWatchLists().first()
        if (watchLists.any { it.title == title }) {
            throw BingeBotException(reason = WATCHLIST_ALREADY_EXISTS)
        }
        val profileId = preferencesDataSource.activeProfileId.first() ?: throw BingeBotException(
            reason = SESSION_EXPIRED
        )
        val watchListId = UUID.randomUUID().toString()
        return suspendCoroutine { continuation ->
            firestore.collection(FIREBASE_COLLECTION_ROOT)
                .document(profileId)
                .collection(firebaseCollectionPaths.ITEM_WATCHLIST_PATH)
                .document(watchListId)
                .set(watchListFactory.create(watchListId, title))
                .addOnSuccessListener {
                    continuation.resume(watchListId)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(BingeBotException(it, DATA_WRITE_ERROR))
                }
        }
    }

    override suspend fun addItemToWatchList(watchListId: String, itemId: Int) {
        val profileId = preferencesDataSource.activeProfileId.first() ?: return
        suspendCoroutine { continuation ->
            firestore.collection(FIREBASE_COLLECTION_ROOT)
                .document(profileId)
                .collection(firebaseCollectionPaths.ITEM_WATCHLIST_PATH)
                .document(watchListId)
                .update(FIELD_ITEM_IDS, FieldValue.arrayUnion(itemId))
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(BingeBotException(it, DATA_WRITE_ERROR))
                }
        }
    }

    override suspend fun removeItemFromWatchList(watchListId: String, itemId: Int) {
        val profileId = preferencesDataSource.activeProfileId.first() ?: return
        suspendCoroutine { continuation ->
            firestore.collection(FIREBASE_COLLECTION_ROOT)
                .document(profileId)
                .collection(firebaseCollectionPaths.ITEM_WATCHLIST_PATH)
                .document(watchListId)
                .update(FIELD_ITEM_IDS, FieldValue.arrayRemove(itemId))
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(BingeBotException(it, DATA_WRITE_ERROR))
                }
        }
    }

    override suspend fun deleteWatchList(watchListId: String) {
        val profileId = preferencesDataSource.activeProfileId.first() ?: return
        suspendCoroutine { continuation ->
            firestore.collection(FIREBASE_COLLECTION_ROOT)
                .document(profileId)
                .collection(firebaseCollectionPaths.ITEM_WATCHLIST_PATH)
                .document(watchListId)
                .delete()
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(BingeBotException(it, DATA_WRITE_ERROR))
                }
        }
    }


    companion object {
        const val FIELD_WATCHDATE = "watchDate"
        const val FIELD_ITEM_IDS = "itemIds"
    }

}