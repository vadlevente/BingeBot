package com.vadlevente.bingebot.authentication.domain.usecase

import com.vadlevente.bingebot.core.data.service.BiometricsService
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IsBiometricsAvailableUseCase @Inject constructor(
    private val biometricsService: BiometricsService,
) : BaseUseCase<Unit, Boolean> {

    override fun execute(params: Unit): Flow<Boolean> =
        flow {
            emit(biometricsService.isBiometricsAvailable())
        }

}