package com.bingebot.core.util

import com.bingebot.core.model.Model
import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder

fun Model.toJson(): String = GsonBuilder().create().toJson(this)

inline fun <reified T> String?.toObject(): T? = this?.run {
    GsonBuilder().create().fromJson<T>(this, object : TypeToken<T>() {}.type)
}