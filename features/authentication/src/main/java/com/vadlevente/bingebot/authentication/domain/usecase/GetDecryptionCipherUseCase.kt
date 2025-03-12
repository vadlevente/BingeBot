package com.vadlevente.bingebot.authentication.domain.usecase

import com.vadlevente.bingebot.core.data.service.SecretService
import com.vadlevente.bingebot.core.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.crypto.Cipher
import javax.inject.Inject

class GetDecryptionCipherUseCase @Inject constructor(
    private val secretService: SecretService,
) : BaseUseCase<Unit, Cipher> {

    override fun execute(params: Unit): Flow<Cipher> = secretService.getDecryptionCipher()

}