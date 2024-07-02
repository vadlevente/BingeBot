package com.vadlevente.bingebot.authentication.domain.usecase

import com.vadlevente.bingebot.core.data.service.AuthenticationService
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class RegistrationUseCaseParams(
    val email: String,
    val password: String,
)

class RegistrationUseCase @Inject constructor(
    private val authenticationService: AuthenticationService,
) : BaseUseCase<RegistrationUseCaseParams, Unit>() {

    override fun execute(params: RegistrationUseCaseParams): Flow<Unit> =
        emptyFlow {
            authenticationService.register(params.email, params.password)
        }

}