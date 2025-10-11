package com.vadlevente.bingebot.core.usecase

import com.vadlevente.bingebot.core.data.cache.ScreenResultDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class SetScreenResultUseCaseArgs(
    val key: String,
)

class SetScreenResultUseCase @Inject constructor(
    private val screenResultDataSource: ScreenResultDataSource,
) : BaseUseCase<SetScreenResultUseCaseArgs, Unit> {

    override fun execute(params: SetScreenResultUseCaseArgs): Flow<Unit> = emptyFlow {
        screenResultDataSource.setResult(params.key)
    }

}