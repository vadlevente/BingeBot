package com.bingebot.splash.usecase

import com.bingebot.core.data.local.datastore.PreferencesDataSource
import com.bingebot.core.data.service.AuthenticationService
import com.bingebot.core.model.NavDestination
import com.bingebot.core.model.NavDestination.LIST
import com.bingebot.core.model.NavDestination.LOGIN
import com.bingebot.core.model.NavDestination.REGISTRATION
import com.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty
import javax.inject.Inject

class GetNavDestinationToStartScreenUseCase @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val preferencesDataSource: PreferencesDataSource,
) : BaseUseCase<Unit, NavDestination>() {

    override fun execute(params: Unit): Flow<NavDestination> =
//        preferencesDataSource.activeProfileId
        flowOf("id")
            .map {
                if (authenticationService.isProfileSignedIn(it)) LIST
                else LOGIN
            }
            .onEmpty { emit(REGISTRATION) }

}