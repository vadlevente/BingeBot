package com.vadlevente.bingebot.core.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface BaseUseCase<Param, Result> {

    fun execute(params: Param): Flow<Result>

    fun emptyFlow(action: suspend () -> Unit) = flow {
        action()
        emit(Unit)
    }

}