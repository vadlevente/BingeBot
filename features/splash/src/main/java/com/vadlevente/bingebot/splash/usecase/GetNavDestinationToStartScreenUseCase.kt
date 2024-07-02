package com.vadlevente.bingebot.splash.usecase

import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.data.service.AuthenticationService
import com.vadlevente.bingebot.core.model.NavDestination
import com.vadlevente.bingebot.core.model.NavDestination.LIST
import com.vadlevente.bingebot.core.model.NavDestination.LOGIN
import com.vadlevente.bingebot.core.model.NavDestination.REGISTRATION
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNavDestinationToStartScreenUseCase @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val preferencesDataSource: PreferencesDataSource,
) : BaseUseCase<Unit, NavDestination>() {

    override fun execute(params: Unit): Flow<NavDestination> {
        return preferencesDataSource.activeProfileId
                .map { profileId ->
                    profileId?.let {
                        if (authenticationService.isProfileSignedIn(it)) LIST
                        else LOGIN
                    } ?: REGISTRATION
                }
    }

}