package com.vadlevente.bingebot.core.usecase

import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class SetDisplayNextToWatchUseCaseArgs(
    val value: Boolean,
)

class SetDisplayNextToWatchUseCase @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource,
) : BaseUseCase<SetDisplayNextToWatchUseCaseArgs, Unit> {

    override fun execute(params: SetDisplayNextToWatchUseCaseArgs): Flow<Unit> = emptyFlow {
        preferencesDataSource.saveDisplayNextToWatch(params.value)
    }

}