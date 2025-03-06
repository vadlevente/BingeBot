package com.vadlevente.bingebot.core.data.service

import android.util.Base64
import com.vadlevente.bingebot.core.model.exception.BingeBotException
import com.vadlevente.bingebot.core.model.exception.Reason
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

interface AESService {
    fun encrypt(plainText: String, password: String): String
    fun decrypt(encryptedText: String, password: String): String
}

class AESServiceImpl @Inject constructor() : AESService {

    override fun encrypt(plainText: String, password: String): String {
        val salt = ByteArray(16).apply { SecureRandom().nextBytes(this) }
        val iv = ByteArray(16).apply { SecureRandom().nextBytes(this) }
        val secretKey = generateKey(password, salt)
        val cipher = Cipher.getInstance(AES_TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(iv))
        }
        val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(salt + iv + encryptedBytes, Base64.NO_WRAP)
    }

    override fun decrypt(encryptedText: String, password: String): String {
        try {
            val decodedBytes = Base64.decode(encryptedText, Base64.NO_WRAP)

            val salt = decodedBytes.copyOfRange(0, 16)
            val iv = decodedBytes.copyOfRange(16, 32)
            val cipherText = decodedBytes.copyOfRange(32, decodedBytes.size)

            val secretKey = generateKey(password, salt)

            val cipher = Cipher.getInstance(AES_TRANSFORMATION).apply {
                init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
            }

            val decryptedBytes = cipher.doFinal(cipherText)
            return String(decryptedBytes, Charsets.UTF_8)
        } catch (t: Throwable) {
            throw BingeBotException(originalException = t, reason = Reason.WRONG_PIN_CODE)
        }
    }

    private fun generateKey(password: String, salt: ByteArray): SecretKey {
        val factory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_ALGORITHM)
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH)
        return SecretKeySpec(factory.generateSecret(spec).encoded, AES_ALGORITHM)
    }

    companion object {
        private const val AES_ALGORITHM = "AES"
        private const val AES_TRANSFORMATION = "AES/CBC/PKCS5Padding"
        private const val SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA256"
        private const val ITERATION_COUNT = 10000
        private const val KEY_LENGTH = 256
    }
}