package com.vadlevente.bingebot.core.data.service

import android.content.Context
import androidx.biometric.BiometricManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface BiometricsService {
    fun isBiometricsAvailable(): Boolean
}

class BiometricsServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : BiometricsService {

    override fun isBiometricsAvailable(): Boolean {
        val biometricManager = BiometricManager.from(context)
        return biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
    }

}