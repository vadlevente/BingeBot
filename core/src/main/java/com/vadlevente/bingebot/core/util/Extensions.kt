package com.vadlevente.bingebot.core.util

import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import com.vadlevente.bingebot.core.model.Model

fun Model.toJson(): String = GsonBuilder().create().toJson(this)

inline fun <reified T> String?.toObject(): T? = this?.run {
    GsonBuilder().create().fromJson<T>(this, object : TypeToken<T>() {}.type)
}