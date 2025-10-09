package com.vadlevente.bingebot.splash.usecase

import com.vadlevente.bingebot.core.data.repository.ConfigurationRepository
import com.vadlevente.bingebot.core.usecase.BaseUseCase
import javax.inject.Inject

class SetDefaultLanguageUseCase @Inject constructor(
    private val configurationRepository: ConfigurationRepository,
    ) : BaseUseCase<Unit, Unit> {

    override fun execute(params: Unit) = emptyFlow {
        configurationRepository.setDefaultLanguage()
    }

}