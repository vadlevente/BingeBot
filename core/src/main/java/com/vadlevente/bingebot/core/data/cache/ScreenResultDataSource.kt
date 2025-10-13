package com.vadlevente.bingebot.core.data.cache

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScreenResultDataSource @Inject constructor() {
    private val _result = MutableSharedFlow<String?>()
    val result = _result.asSharedFlow()

    suspend fun setResult(key: String) {
        _result.emit(key)
    }
}