package com.vadlevente.bingebot.authentication.domain.usecase

import com.vadlevente.bingebot.core.data.service.AuthenticationService
import com.vadlevente.bingebot.core.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class ResendPasswordUseCaseParams(
    val email: String,
)

class ResendPasswordUseCase @Inject constructor(
    private val authenticationService: AuthenticationService,
) : BaseUseCase<ResendPasswordUseCaseParams, Unit> {

    override fun execute(params: ResendPasswordUseCaseParams): Flow<Unit> =
        emptyFlow {
            authenticationService.resendPassword(params.email)
        }

}