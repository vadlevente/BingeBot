package com.vadlevente.bingebot.core.data.service

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.model.exception.BingeBotException
import com.vadlevente.bingebot.core.model.exception.Reason.AUTHENTICATION_FAILED
import com.vadlevente.bingebot.core.model.exception.Reason.SESSION_EXPIRED
import com.vadlevente.bingebot.core.model.exception.Reason.WEAK_PASSWORD
import dagger.Lazy
import javax.inject.Inject
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface AuthenticationService {
    val currentUserId: String?
    suspend fun login(email: String, password: String)
    suspend fun register(email: String, password: String)
    suspend fun isProfileSignedIn(profileId: String): Boolean
}

class AuthenticationServiceImpl @Inject constructor(
    private val firebaseAuth: Lazy<FirebaseAuth>,
    private val preferencesDataSource: PreferencesDataSource,
) : AuthenticationService {

    override val currentUserId: String?
        get() = firebaseAuth.get().currentUser?.uid

    override suspend fun login(email: String, password: String) {
        suspendCoroutine { continuation ->
            firebaseAuth.get().signInWithEmailAndPassword(email, password)
                .handleAuthResult(continuation)
        }
        saveCurrentUserId()
    }

    override suspend fun register(email: String, password: String) {
        suspendCoroutine { continuation ->
            firebaseAuth.get().createUserWithEmailAndPassword(email, password)
                .handleAuthResult(continuation)
        }
        saveCurrentUserId()
    }

    override suspend fun isProfileSignedIn(profileId: String): Boolean {
        if (profileId != currentUserId) {
            return false
        }
        return suspendCoroutine { continuation ->
            firebaseAuth.get().currentUser?.reload()
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        continuation.resume(true)
                    } else {
                        it.exception?.let {
                            continuation.resumeWithException(BingeBotException(SESSION_EXPIRED))
                        } ?: continuation.resume(false)
                    }
                }
        }
    }

    private suspend fun saveCurrentUserId() {
        currentUserId?.let {
            preferencesDataSource.saveActiveProfileId(it)
        } ?: throw BingeBotException(AUTHENTICATION_FAILED)
    }

    private fun Task<AuthResult>.handleAuthResult(continuation: Continuation<Unit>) =
        addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(Unit)
            } else {
                it.exception?.let { throwable ->
                    val exception = if (throwable is FirebaseAuthWeakPasswordException) {
                        BingeBotException(WEAK_PASSWORD)
                    } else throwable
                    continuation.resumeWithException(exception)
                } ?: continuation.resume(Unit)
            }
        }
}