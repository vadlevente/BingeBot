package com.bingebot.core.data.authentication

interface AuthenticationService {
    suspend fun login(email: String, password: String)
    suspend fun register(email: String, password: String)
    suspend fun isProfileSignedIn(id: String): Boolean
}

class AuthenticationServiceImpl : AuthenticationService {

    override suspend fun login(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun register(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun isProfileSignedIn(id: String): Boolean {
        TODO("Not yet implemented")
    }
}