package com.bingebot.core.util

import com.bingebot.core.model.Model
import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable
import kotlin.reflect.KClass

fun Model.toJson(): String = GsonBuilder().create().toJson(this)

inline fun <reified T> String?.toObject(): T? = this?.run {
    GsonBuilder().create().fromJson<T>(this, object : TypeToken<T>() {}.type)
}

fun <T : Any> String?.toObject(klass: KClass<T>): T? = this?.let {
    GsonBuilder().create().fromJson(this, klass.java)
}

fun <T : Any> getTypeAsClass(instance: Any, index: Int = 0): KClass<T> =
    instance.javaClass.genericSuperclass.let { superclass ->
        if (superclass is Class<*>) throw IllegalArgumentException("getTypeAsClass(): superclass(${superclass::class.qualifiedName}) of the instance(${instance::class.qualifiedName}) is not generic.")
        (superclass as ParameterizedType).actualTypeArguments[index].let<Type?, KClass<T>> { type ->
            if (type is TypeVariable<*>) throw IllegalArgumentException("getTypeAsClass(): type(${type.name}) is not specified.")
            @Suppress("UNCHECKED_CAST")
            return ((if (type is ParameterizedType) type.rawType else type) as Class<T>).kotlin
        }
    }