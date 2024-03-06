package com.bingebot.authentication.domain.usecase

import com.bingebot.core.data.service.AuthenticationService
import com.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

data class RegistrationUseCaseParams(
    val email: String,
    val password: String,
)

class RegistrationUseCase @Inject constructor(
    private val authenticationService: AuthenticationService,
) : BaseUseCase<RegistrationUseCaseParams, Unit>() {

    override fun execute(params: RegistrationUseCaseParams): Flow<Unit> =
        flow {
            authenticationService.register(params.email, params.password)
            emit(Unit)
        }

}