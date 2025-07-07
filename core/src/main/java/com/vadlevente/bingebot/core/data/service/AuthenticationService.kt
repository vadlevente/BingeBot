package com.vadlevente.bingebot.core.data.service

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.model.exception.BingeBotException
import com.vadlevente.bingebot.core.model.exception.Reason.AUTHENTICATION_FAILED
import com.vadlevente.bingebot.core.model.exception.Reason.EMAIL_ALREADY_IN_USE
import com.vadlevente.bingebot.core.model.exception.Reason.INVALID_CREDENTIALS
import com.vadlevente.bingebot.core.model.exception.Reason.NON_EXISTENT_USER
import com.vadlevente.bingebot.core.model.exception.Reason.RESEND_UNSUCCESSFUL
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
    val currentUserEmail: String?
    suspend fun login(email: String, password: String)
    suspend fun register(email: String, password: String): String
    suspend fun isProfileSignedIn(profileId: String): Boolean
    suspend fun logout()
    suspend fun resendPassword(email: String)
}

class AuthenticationServiceImpl @Inject constructor(
    private val firebaseAuth: Lazy<FirebaseAuth>,
    private val preferencesDataSource: PreferencesDataSource,
) : AuthenticationService {

    override val currentUserId: String?
        get() = firebaseAuth.get().currentUser?.uid

    override val currentUserEmail: String?
        get() = firebaseAuth.get().currentUser?.email

    override suspend fun login(email: String, password: String) {
        suspendCoroutine { continuation ->
            firebaseAuth.get().signInWithEmailAndPassword(email, password)
                .handleAuthResult(continuation)
        }
        saveCurrentUserId()
    }

    override suspend fun register(email: String, password: String): String {
        suspendCoroutine { continuation ->
            firebaseAuth.get().createUserWithEmailAndPassword(email, password)
                .handleAuthResult(continuation)
        }
        saveCurrentUserId()
        return currentUserId!!
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
                            continuation.resumeWithException(BingeBotException(it, SESSION_EXPIRED))
                        } ?: continuation.resume(false)
                    }
                }
        }
    }

    override suspend fun logout() {
        firebaseAuth.get().signOut()
    }

    override suspend fun resendPassword(email: String) {
        return suspendCoroutine { continuation ->
            firebaseAuth.get().sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isSuccessful) {
                    continuation.resume(Unit)
                } else {
                    it.exception?.let {
                        continuation.resumeWithException(BingeBotException(it, RESEND_UNSUCCESSFUL))
                    } ?: continuation.resume(Unit)
                }
            }
        }
    }

    private suspend fun saveCurrentUserId() {
        currentUserId?.let {
            preferencesDataSource.saveActiveProfileId(it)
        } ?: throw BingeBotException(reason = AUTHENTICATION_FAILED)
    }

    private fun Task<AuthResult>.handleAuthResult(continuation: Continuation<Unit>) =
        addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(Unit)
            } else {
                it.exception?.let { throwable ->
                    val exception = when (throwable) {
                        is FirebaseAuthWeakPasswordException -> BingeBotException(throwable, WEAK_PASSWORD)
                        is FirebaseAuthInvalidCredentialsException -> BingeBotException(throwable, INVALID_CREDENTIALS)
                        is FirebaseAuthUserCollisionException -> BingeBotException(throwable, EMAIL_ALREADY_IN_USE)
                        is FirebaseAuthInvalidUserException -> BingeBotException(throwable, NON_EXISTENT_USER)
                        else -> throwable
                    }
                    continuation.resumeWithException(exception)
                } ?: continuation.resume(Unit)
            }
        }
}