package com.vadlevente.bingebot.core.model.firestore

import java.util.Date

data class StoredMovie(
    val id: String,
    val watchDate: Date? = null,
)
