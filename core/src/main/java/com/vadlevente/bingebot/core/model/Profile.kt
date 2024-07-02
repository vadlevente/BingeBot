package com.vadlevente.bingebot.core.model

data class Profile(
    val id: String,
    val emailAddress: String,
    var color: Long? = null,
)
