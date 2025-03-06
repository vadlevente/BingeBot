package com.vadlevente.bingebot.authentication.domain.usecase

import com.vadlevente.bingebot.core.data.service.SecretService
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.crypto.Cipher
import javax.inject.Inject

data class SaveSecretWithBiometricsUseCaseParams(
    val cipher: Cipher,
)

class SaveSecretWithBiometricsUseCase @Inject constructor(
    private val secretService: SecretService,
) : BaseUseCase<SaveSecretWithBiometricsUseCaseParams, Unit> {

    override fun execute(params: SaveSecretWithBiometricsUseCaseParams): Flow<Unit> =
        emptyFlow {
            secretService.saveCredentialsWithBiometrics(params.cipher)
        }

}