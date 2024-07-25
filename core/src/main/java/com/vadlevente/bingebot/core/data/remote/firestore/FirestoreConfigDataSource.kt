package com.vadlevente.bingebot.core.data.remote.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.vadlevente.bingebot.core.model.exception.BingeBotException
import com.vadlevente.bingebot.core.model.exception.Reason.DATA_WRITE_ERROR
import com.vadlevente.bingebot.core.model.firestore.StoredData
import com.vadlevente.bingebot.core.util.Constants.FIREBASE_COLLECTION_ROOT
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirestoreConfigDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
) {

    suspend fun createUser(userId: String) = suspendCoroutine { continuation ->
        firestore.collection(FIREBASE_COLLECTION_ROOT)
            .document(userId)
            .set(StoredData())
            .addOnSuccessListener {
                continuation.resume(Unit)
            }
            .addOnFailureListener {
                continuation.resumeWithException(BingeBotException(it, DATA_WRITE_ERROR))
            }
    }

}