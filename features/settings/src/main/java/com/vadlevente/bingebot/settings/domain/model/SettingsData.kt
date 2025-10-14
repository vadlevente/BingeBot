package com.vadlevente.bingebot.settings.domain.model

import com.vadlevente.bingebot.core.model.SelectedLanguage

data class SettingsData(
    val languages: Map<SelectedLanguage, Boolean>,
    val email: String?,
    val hasStoredSecret: Boolean,
    val displayNextToWatch: Boolean,
)
