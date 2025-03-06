package com.vadlevente.bingebot.core.ui.composables

import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.CryptoObject
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.crypto.Cipher

@Composable
fun BBBiometricPrompt(
    title: String,
    negativeButtonText: String,
    cipher: Cipher,
    onAuthSuccessful: (Cipher) -> Unit,
    onAuthCancelled: () -> Unit,
) {
    val activity = LocalContext.current as FragmentActivity
    val executor: Executor = Executors.newSingleThreadExecutor()
    val biometricPrompt = BiometricPrompt(
        activity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onAuthSuccessful(result.cryptoObject?.cipher!!)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                if (errorCode in listOf(
                        BiometricPrompt.ERROR_CANCELED,
                        BiometricPrompt.ERROR_NEGATIVE_BUTTON,
                        BiometricPrompt.ERROR_USER_CANCELED,
                    )
                ) {
                    onAuthCancelled()
                }
            }
        })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(title)
        .setNegativeButtonText(negativeButtonText)
        .build()

    biometricPrompt.authenticate(promptInfo, CryptoObject(cipher))
}