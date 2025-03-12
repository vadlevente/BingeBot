package com.vadlevente.bingebot.settings.domain.usecases

import com.vadlevente.bingebot.core.data.repository.ConfigurationRepository
import com.vadlevente.bingebot.core.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserEmailUseCase @Inject constructor(
    private val configurationRepository: ConfigurationRepository,
) : BaseUseCase<Unit, String?> {

    override fun execute(params: Unit): Flow<String?> = flow {
        emit(configurationRepository.getUserEmail())
    }

}