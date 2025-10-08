package com.vadlevente.bingebot.core.usecase

import com.vadlevente.bingebot.core.data.service.SecretService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HasStoredSecretUseCase @Inject constructor(
    private val secretService: SecretService,
) : BaseUseCase<Unit, Boolean> {

    override fun execute(params: Unit): Flow<Boolean> = secretService.hasStoredSecret

}