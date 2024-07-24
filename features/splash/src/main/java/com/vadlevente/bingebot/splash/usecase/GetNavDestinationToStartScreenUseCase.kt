package com.vadlevente.bingebot.splash.usecase

import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.data.service.AuthenticationService
import com.vadlevente.bingebot.core.model.NavDestination
import com.vadlevente.bingebot.core.model.NavDestination.DASHBOARD
import com.vadlevente.bingebot.core.model.NavDestination.LOGIN
import com.vadlevente.bingebot.core.model.NavDestination.REGISTRATION
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetNavDestinationToStartScreenUseCase @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val preferencesDataSource: PreferencesDataSource,
) : BaseUseCase<Unit, NavDestination> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(params: Unit): Flow<NavDestination> {
        return preferencesDataSource.activeProfileId
            .flatMapLatest { profileId ->
                profileId?.let {
                    if (authenticationService.isProfileSignedIn(it)) flowOf(DASHBOARD)
                    else flowOf(LOGIN)
                } ?: flowOf(REGISTRATION)
            }
    }

}