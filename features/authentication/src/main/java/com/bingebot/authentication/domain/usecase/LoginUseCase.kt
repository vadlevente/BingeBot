package com.bingebot.authentication.domain.usecase

import com.bingebot.core.data.service.AuthenticationService
import com.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

data class LoginUseCaseParams(
    val email: String,
    val password: String,
)

class LoginUseCase @Inject constructor(
    private val authenticationService: AuthenticationService,
) : BaseUseCase<LoginUseCaseParams, Unit>() {

    override fun execute(params: LoginUseCaseParams): Flow<Unit> =
        flow {
            authenticationService.login(params.email, params.password)
            emit(Unit)
        }

}