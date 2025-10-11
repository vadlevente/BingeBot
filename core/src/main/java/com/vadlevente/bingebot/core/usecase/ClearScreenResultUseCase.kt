package com.vadlevente.bingebot.core.usecase

import com.vadlevente.bingebot.core.data.cache.ScreenResultDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ClearScreenResultUseCase @Inject constructor(
    private val screenResultDataSource: ScreenResultDataSource,
) : BaseUseCase<Unit, Unit> {

    override fun execute(params: Unit): Flow<Unit> = emptyFlow {
        screenResultDataSource.clearResult()
    }

}