package com.vadlevente.bingebot.core.usecase

import com.vadlevente.bingebot.core.data.service.SecretService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TurnOffAuthenticationUseCase @Inject constructor(
    private val secretService: SecretService,
) : BaseUseCase<Unit, Unit> {

    override fun execute(params: Unit): Flow<Unit> = emptyFlow {
        secretService.deleteSecretData()
    }

}