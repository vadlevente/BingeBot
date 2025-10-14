package com.vadlevente.bingebot.core.usecase

import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDisplayNextToWatchUseCase @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource,
) : BaseUseCase<Unit, Boolean> {

    override fun execute(params: Unit): Flow<Boolean> = preferencesDataSource.displayNextToWatch

}