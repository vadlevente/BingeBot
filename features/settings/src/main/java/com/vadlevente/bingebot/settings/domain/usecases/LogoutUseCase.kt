package com.vadlevente.bingebot.settings.domain.usecases

import com.vadlevente.bingebot.core.data.repository.ConfigurationRepository
import com.vadlevente.bingebot.core.ui.BaseUseCase
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val configurationRepository: ConfigurationRepository,
) : BaseUseCase<Unit, Unit> {

    override fun execute(params: Unit) = emptyFlow {
        configurationRepository.logout()
    }

}