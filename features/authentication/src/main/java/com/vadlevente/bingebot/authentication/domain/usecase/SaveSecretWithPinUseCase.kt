package com.vadlevente.bingebot.authentication.domain.usecase

import com.vadlevente.bingebot.core.data.service.SecretService
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class SaveSecretWithPinUseCaseParams(
    val pin: String,
)

class SaveSecretWithPinUseCase @Inject constructor(
    private val secretService: SecretService,
) : BaseUseCase<SaveSecretWithPinUseCaseParams, Unit> {

    override fun execute(params: SaveSecretWithPinUseCaseParams): Flow<Unit> =
        emptyFlow {
            secretService.saveCredentialsWithPin(params.pin)
        }

}