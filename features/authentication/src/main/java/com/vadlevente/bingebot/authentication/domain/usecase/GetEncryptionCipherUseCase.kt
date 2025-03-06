package com.vadlevente.bingebot.authentication.domain.usecase

import com.vadlevente.bingebot.core.data.service.CryptographyService
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.crypto.Cipher
import javax.inject.Inject

class GetEncryptionCipherUseCase @Inject constructor(
    private val cryptographyService: CryptographyService
) : BaseUseCase<Unit, Cipher> {

    override fun execute(params: Unit): Flow<Cipher> = flow{
        emit(cryptographyService.getInitializedCipherForEncryption())
    }

}