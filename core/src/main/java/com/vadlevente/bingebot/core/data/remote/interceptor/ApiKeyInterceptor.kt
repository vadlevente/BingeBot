package com.vadlevente.bingebot.core.data.remote.interceptor

import com.vadlevente.bingebot.core.BuildConfig
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(HEADER_AUTHORIZATION, "$BEARER ${BuildConfig.ACCESS_TOKEN}")
            .build()
        return chain.proceed(request)
    }

    companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val BEARER = "Bearer"
    }
}