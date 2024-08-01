package com.vadlevente.bingebot.settings.domain.usecases

import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.model.SelectedLanguage
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLanguagesUseCase @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource,
) : BaseUseCase<Unit, Map<SelectedLanguage, Boolean>> {

    override fun execute(params: Unit): Flow<Map<SelectedLanguage, Boolean>> =
        preferencesDataSource.language.map { selectedLanguage ->
            SelectedLanguage.values().associateWith {
                it.code == selectedLanguage.code
            }
        }

}