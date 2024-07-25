package com.vadlevente.bingebot.core.model.firestore

import java.util.Date

data class StoredItem(
    val id: String,
    val watchDate: Date? = null,
    val createdDate: Date,
) {
    constructor() : this("", null, Date())
}
