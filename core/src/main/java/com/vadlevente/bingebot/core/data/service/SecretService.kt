package com.vadlevente.bingebot.core.data.service

import android.util.Base64
import com.google.gson.Gson
import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.model.EncryptedData
import com.vadlevente.bingebot.core.model.LoginCredentials
import com.vadlevente.bingebot.core.model.exception.BingeBotException
import com.vadlevente.bingebot.core.model.exception.Reason
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.crypto.Cipher
import javax.inject.Inject

interface SecretService {
    fun setTempCredentials(
        email: String,
        password: String,
    )

    suspend fun saveCredentialsWithPin(pin: String)
    fun retrieveCredentialsWithPin(pin: String): Flow<LoginCredentials?>
    suspend fun saveCredentialsWithBiometrics(cipher: Cipher)
    fun retrieveCredentialsWithBiometrics(cipher: Cipher): Flow<LoginCredentials?>
    fun getDecryptionCipher(): Flow<Cipher>
}

class SecretServiceImpl @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource,
    private val aesService: AESService,
    private val cryptographyService: CryptographyService,
    private val gson: Gson,
) : SecretService {

    private var tempCredentials: LoginCredentials? = null

    override fun setTempCredentials(email: String, password: String) {
        tempCredentials = LoginCredentials(email, password)
    }

    override suspend fun saveCredentialsWithPin(pin: String) {
        if (tempCredentials == null) throw BingeBotException()
        val plainText = gson.toJson(tempCredentials)
        val encrypted = aesService.encrypt(plainText, pin)
        preferencesDataSource.savePinEncryptedSecret(encrypted)
    }

    override fun retrieveCredentialsWithPin(pin: String) =
        preferencesDataSource.pinEncryptedSecret.map {
            it?.let { aesService.decrypt(it, pin) }?.let {
                gson.fromJson(it, LoginCredentials::class.java)
            } ?: throw BingeBotException(reason = Reason.WRONG_PIN_CODE)
        }

    override suspend fun saveCredentialsWithBiometrics(cipher: Cipher) {
        if (tempCredentials == null) throw BingeBotException()
        val plainText = gson.toJson(tempCredentials)

        val encrypted = cryptographyService.encryptData(plainText, cipher)
        preferencesDataSource.saveBiometricsEncryptedSecret(encrypted)
    }

    override fun retrieveCredentialsWithBiometrics(cipher: Cipher) =
        preferencesDataSource.biometricsEncryptedSecret.map {
            val encrypted =
                gson.fromJson(String(Base64.decode(it, Base64.DEFAULT)), EncryptedData::class.java)
            it?.let { cryptographyService.decryptData(encrypted.ciphertext, cipher) }?.let {
                gson.fromJson(it, LoginCredentials::class.java)
            } ?: throw BingeBotException(reason = Reason.AUTHENTICATION_FAILED)
        }

    override fun getDecryptionCipher() =
        preferencesDataSource.biometricsEncryptedSecret.map {
            val encrypted =
                gson.fromJson(String(Base64.decode(it, Base64.DEFAULT)), EncryptedData::class.java)
            cryptographyService.getInitializedCipherForDecryption(encrypted.initializationVector)
        }
}