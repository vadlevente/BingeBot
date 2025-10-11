package com.vadlevente.bingebot.core.usecase

import com.vadlevente.bingebot.core.data.cache.ScreenResultDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class GetScreenResultUseCaseArgs(
    val key: String,
)

class GetScreenResultUseCase @Inject constructor(
    private val screenResultDataSource: ScreenResultDataSource,
) : BaseUseCase<GetScreenResultUseCaseArgs, Boolean> {

    override fun execute(params: GetScreenResultUseCaseArgs): Flow<Boolean> =
        screenResultDataSource.result.map {
            it == params.key
        }

}