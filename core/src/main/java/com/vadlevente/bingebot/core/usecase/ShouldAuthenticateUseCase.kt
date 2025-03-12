package com.vadlevente.bingebot.core.usecase

import com.vadlevente.bingebot.core.data.service.SecretService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ShouldAuthenticateUseCase @Inject constructor(
    private val secretService: SecretService,
) : BaseUseCase<Unit, Boolean> {

    override fun execute(params: Unit): Flow<Boolean> = secretService.isAuthenticated.map { !it }

}