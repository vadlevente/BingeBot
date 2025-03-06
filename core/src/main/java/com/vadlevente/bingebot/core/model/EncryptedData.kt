package com.vadlevente.bingebot.core.model

data class EncryptedData(
    val ciphertext: ByteArray,
    val initializationVector: ByteArray
)