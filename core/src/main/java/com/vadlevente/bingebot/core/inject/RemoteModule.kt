package com.vadlevente.bingebot.core.inject

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.vadlevente.bingebot.core.BuildConfig
import com.vadlevente.bingebot.core.data.api.ConfigurationApi
import com.vadlevente.bingebot.core.data.api.MovieApi
import com.vadlevente.bingebot.core.data.api.PersonApi
import com.vadlevente.bingebot.core.data.api.TvApi
import com.vadlevente.bingebot.core.data.remote.interceptor.ApiKeyInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        gson: Gson,
    ): Retrofit =
        Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BuildConfig.BASE_URL)
            .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        apiKeyInterceptor: ApiKeyInterceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { setLevel(BODY) })
            .addInterceptor(apiKeyInterceptor)
            .connectTimeout(30, SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideMovieApi(
        retrofit: Retrofit
    ): MovieApi = retrofit.create(MovieApi::class.java)

    @Provides
    @Singleton
    fun provideTvApi(
        retrofit: Retrofit
    ): TvApi = retrofit.create(TvApi::class.java)

    @Provides
    @Singleton
    fun provideConfigurationApi(
        retrofit: Retrofit
    ): ConfigurationApi = retrofit.create(ConfigurationApi::class.java)

    @Provides
    @Singleton
    fun providePersonApi(
        retrofit: Retrofit
    ): PersonApi = retrofit.create(PersonApi::class.java)

}