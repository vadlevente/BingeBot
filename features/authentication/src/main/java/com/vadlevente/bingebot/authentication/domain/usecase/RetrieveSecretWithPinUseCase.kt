package com.vadlevente.bingebot.authentication.domain.usecase

import com.vadlevente.bingebot.core.data.service.AuthenticationService
import com.vadlevente.bingebot.core.data.service.SecretService
import com.vadlevente.bingebot.core.usecase.BaseUseCase
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class RetrieveSecretWithPinUseCaseParams(
    val pin: String,
)

class RetrieveSecretWithPinUseCase @Inject constructor(
    private val secretService: SecretService,
    private val authenticationService: AuthenticationService,
) : BaseUseCase<RetrieveSecretWithPinUseCaseParams, Unit> {

    override fun execute(params: RetrieveSecretWithPinUseCaseParams) =
        secretService.retrieveCredentialsWithPin(params.pin).map {
            it?.let { credentials ->
                if (!authenticationService.isProfileSignedIn(authenticationService.currentUserId!!)) {
                    authenticationService.login(it.email, it.password)
                }
                secretService.setAuthenticated(true)
            }
            Unit
        }

}