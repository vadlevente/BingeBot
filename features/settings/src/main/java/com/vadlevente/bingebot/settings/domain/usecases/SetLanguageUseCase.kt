package com.vadlevente.bingebot.settings.domain.usecases

import com.vadlevente.bingebot.core.data.repository.ConfigurationRepository
import com.vadlevente.bingebot.core.model.SelectedLanguage
import com.vadlevente.bingebot.core.ui.BaseUseCase
import javax.inject.Inject

data class SetLanguageUseCaseParams(
    val selectedLanguage: SelectedLanguage,
)
class SetLanguageUseCase @Inject constructor(
    private val configurationRepository: ConfigurationRepository,
    ) : BaseUseCase<SetLanguageUseCaseParams, Unit> {

    override fun execute(params: SetLanguageUseCaseParams) = emptyFlow {
        configurationRepository.setSelectedLanguage(params.selectedLanguage)
    }

}