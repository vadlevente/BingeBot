package com.vadlevente.bingebot.core.inject

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
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

}