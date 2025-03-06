package com.vadlevente.bingebot.authentication.domain.usecase

import com.vadlevente.bingebot.core.data.service.AuthenticationService
import com.vadlevente.bingebot.core.data.service.SecretService
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.map
import javax.crypto.Cipher
import javax.inject.Inject

data class RetrieveSecretWithBiometricsUseCaseParams(
    val cipher: Cipher,
)

class RetrieveSecretWithBiometricsUseCase @Inject constructor(
    private val secretService: SecretService,
    private val authenticationService: AuthenticationService,
) : BaseUseCase<RetrieveSecretWithBiometricsUseCaseParams, Unit> {

    override fun execute(params: RetrieveSecretWithBiometricsUseCaseParams) =
        secretService.retrieveCredentialsWithBiometrics(params.cipher).map {
            it?.let {
                authenticationService.login(it.email, it.password)
            }
            Unit
        }

}