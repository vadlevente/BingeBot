package com.vadlevente.bingebot.authentication.domain.usecase

import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.data.service.BiometricsService
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class IsBiometricsEnrolledUseCase @Inject constructor(
    private val biometricsService: BiometricsService,
    private val preferencesDataSource: PreferencesDataSource,
) : BaseUseCase<Unit, Boolean> {

    override fun execute(params: Unit): Flow<Boolean> = preferencesDataSource.biometricsEncryptedSecret.map {
        it != null && biometricsService.isBiometricsAvailable()
    }

}