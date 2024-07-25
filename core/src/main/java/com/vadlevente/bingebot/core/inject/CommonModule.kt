package com.vadlevente.bingebot.core.inject

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.vadlevente.bingebot.core.model.GenreFactory
import com.vadlevente.bingebot.core.model.GenreFactory.MovieGenreFactory
import com.vadlevente.bingebot.core.model.GenreFactory.TvGenreFactory
import com.vadlevente.bingebot.core.model.Item.Movie
import com.vadlevente.bingebot.core.model.Item.Tv
import com.vadlevente.bingebot.core.model.WatchListFactory
import com.vadlevente.bingebot.core.model.WatchListFactory.MovieWatchListFactory
import com.vadlevente.bingebot.core.model.WatchListFactory.TvWatchListFactory
import com.vadlevente.bingebot.core.model.config.FirebaseCollectionPaths
import com.vadlevente.bingebot.core.model.config.FirebaseCollectionPaths.MovieFirebaseCollectionPaths
import com.vadlevente.bingebot.core.model.config.FirebaseCollectionPaths.TvFirebaseCollectionPaths
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .registerTypeAdapter(Date::class.java, object : JsonDeserializer<Date> {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            override fun deserialize(
                json: JsonElement?,
                typeOfT: Type?,
                context: JsonDeserializationContext?
            ): Date? {
                if (json?.asString.isNullOrEmpty()) return null
                return json?.asString?.let { dateFormat.parse(it) }
            }
        })
        .create()



    @Provides
    @Singleton
    fun provideTvFirebaseCollectionPaths(): FirebaseCollectionPaths<Tv> = TvFirebaseCollectionPaths

    @Provides
    @Singleton
    fun provideMovieFirebaseCollectionPaths(): FirebaseCollectionPaths<Movie> = MovieFirebaseCollectionPaths

    @Provides
    @Singleton
    fun provideMovieWatchListFactory(): WatchListFactory<Movie> = MovieWatchListFactory

    @Provides
    @Singleton
    fun provideTvWatchListFactory(): WatchListFactory<Tv> = TvWatchListFactory

    @Provides
    @Singleton
    fun provideMovieGenreFactory(): GenreFactory<Movie> = MovieGenreFactory

    @Provides
    @Singleton
    fun provideTvGenreFactory(): GenreFactory<Tv> = TvGenreFactory

}