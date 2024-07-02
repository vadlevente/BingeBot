package com.vadlevente.bingebot.core.model

interface Model

interface ModelWithId<T> : Model {
    val id: T
}