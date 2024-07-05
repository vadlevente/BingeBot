package com.vadlevente.bingebot.core.data.remote.interceptor

import com.vadlevente.bingebot.core.BuildConfig
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(API_KEY, BuildConfig.API_KEY)
            .build()
        return chain.proceed(request)
    }

    companion object {
        private const val API_KEY = "API_KEY"
    }
}