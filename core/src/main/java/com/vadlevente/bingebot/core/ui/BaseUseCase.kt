package com.vadlevente.bingebot.core.ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class BaseUseCase<Param, Result> {

    abstract fun execute(params: Param): Flow<Result>

    fun emptyFlow(action: suspend () -> Unit) = flow {
        action()
        emit(Unit)
    }

}