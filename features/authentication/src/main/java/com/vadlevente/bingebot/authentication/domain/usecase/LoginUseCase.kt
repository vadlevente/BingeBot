package com.vadlevente.bingebot.authentication.domain.usecase

import com.vadlevente.bingebot.core.data.service.AuthenticationService
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class LoginUseCaseParams(
    val email: String,
    val password: String,
)

class LoginUseCase @Inject constructor(
    private val authenticationService: AuthenticationService,
) : BaseUseCase<LoginUseCaseParams, Unit> {

    override fun execute(params: LoginUseCaseParams): Flow<Unit> =
        emptyFlow {
            authenticationService.login(params.email, params.password)
        }

}