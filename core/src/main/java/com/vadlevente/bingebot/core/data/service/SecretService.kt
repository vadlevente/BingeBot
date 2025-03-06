package com.vadlevente.bingebot.core.data.service

import android.util.Base64
import com.google.gson.Gson
import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.model.LoginCredentials
import com.vadlevente.bingebot.core.model.exception.BingeBotException
import com.vadlevente.bingebot.core.model.exception.Reason
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

interface SecretService {
    fun setTempCredentials(
        email: String,
        password: String,
    )
    suspend fun saveCredentialsWithPin(pin: String)
    fun retrieveCredentialsWithPin(pin: String): Flow<LoginCredentials?>
}

class SecretServiceImpl @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource,
    private val gson: Gson,
) : SecretService {

    private var tempCredentials: LoginCredentials? = null

    override fun setTempCredentials(email: String, password: String) {
        tempCredentials = LoginCredentials(email, password)
    }

    override suspend fun saveCredentialsWithPin(pin: String) {
        if (tempCredentials == null) throw BingeBotException()
        val plainText = gson.toJson(tempCredentials)
        val encrypted = encrypt(plainText, pin)
        preferencesDataSource.savePinEncryptedSecret(encrypted)
    }

    override fun retrieveCredentialsWithPin(pin: String) =
        preferencesDataSource.pinEncryptedSecret.map {
            it?.let { decrypt(it, pin) }?.let {
                gson.fromJson(it, LoginCredentials::class.java)
            } ?: throw BingeBotException(reason = Reason.WRONG_PIN_CODE)
        }

    private fun encrypt(plainText: String, password: String): String {
        val salt = ByteArray(16).apply { SecureRandom().nextBytes(this) }
        val iv = ByteArray(16).apply { SecureRandom().nextBytes(this) }
        val secretKey = generateKey(password, salt)

        val cipher = Cipher.getInstance(AES_TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(iv))
        }

        val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

        // Combine Salt + IV + CipherText and encode as Base64
        return Base64.encodeToString(salt + iv + encryptedBytes, Base64.NO_WRAP)
    }

    /**
     * Decrypts an AES encrypted string using the password.
     */
    private fun decrypt(encryptedText: String, password: String): String {
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

    /**
     * Generates a SecretKey using PBKDF2 with the given password and salt.
     */
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