package com.bingebot.core.model

data class Profile(
    val id: String,
    val emailAddress: String,
    var color: Long? = null,
)
