package com.bingebot.core.ui

import kotlinx.coroutines.flow.Flow

abstract class BaseUseCase<Param, Result> {

    abstract fun execute(params: Param): Flow<Result>

}