package com.vadlevente.bingebot.authentication.domain.usecase

import com.vadlevente.bingebot.core.data.service.SecretService
import com.vadlevente.bingebot.core.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.crypto.Cipher
import javax.inject.Inject

data class SaveSecretWithBiometricsUseCaseParams(
    val cipher: Cipher,
    val email: String,
    val password: String,
)

class SaveSecretWithBiometricsUseCase @Inject constructor(
    private val secretService: SecretService,
) : BaseUseCase<SaveSecretWithBiometricsUseCaseParams, Unit> {

    override fun execute(params: SaveSecretWithBiometricsUseCaseParams): Flow<Unit> =
        emptyFlow {
            secretService.saveCredentialsWithBiometrics(
                params.cipher,
                params.email,
                params.password,
            )
            secretService.setAuthenticated(true)
        }

}