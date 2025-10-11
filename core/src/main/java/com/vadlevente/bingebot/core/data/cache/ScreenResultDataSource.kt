package com.vadlevente.bingebot.core.data.cache

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScreenResultDataSource @Inject constructor() {
    private val _result = MutableStateFlow<String?>(null)
    val result = _result.asStateFlow()

    fun setResult(key: String) {
        _result.update { key }
    }

    fun clearResult() {
        _result.update { null }
    }
}