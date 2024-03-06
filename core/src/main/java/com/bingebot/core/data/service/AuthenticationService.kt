package com.bingebot.core.data.service

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import dagger.Lazy
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface AuthenticationService {
    val currentUserId: String?
    suspend fun login(email: String, password: String)
    suspend fun register(email: String, password: String)
    fun isProfileSignedIn(profileId: String): Boolean
}

class AuthenticationServiceImpl(
    private val firebaseAuth: Lazy<FirebaseAuth>,
) : AuthenticationService {

    override val currentUserId: String?
        get() = firebaseAuth.get().currentUser?.uid

    override suspend fun login(email: String, password: String) = suspendCoroutine { continuation ->
        firebaseAuth.get().signInWithEmailAndPassword(email, password)
            .handleAuthResult(continuation)
    }

    override suspend fun register(email: String, password: String) = suspendCoroutine { continuation ->
        firebaseAuth.get().createUserWithEmailAndPassword(email, password)
            .handleAuthResult(continuation)
    }

    override fun isProfileSignedIn(profileId: String) =
        profileId == firebaseAuth.get().currentUser?.uid

    private fun Task<AuthResult>.handleAuthResult(continuation: Continuation<Unit>) =
        addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(Unit)
            } else {
                it.exception?.let { throwable ->
                    continuation.resumeWithException(throwable)
                } ?: continuation.resume(Unit)
            }
        }
}