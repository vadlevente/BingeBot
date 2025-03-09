package com.vadlevente.bingebot.splash.usecase

import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.data.service.AuthenticationService
import com.vadlevente.bingebot.core.model.NavDestination
import com.vadlevente.bingebot.core.ui.BaseUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetNavDestinationToStartScreenUseCase @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val preferencesDataSource: PreferencesDataSource,
) : BaseUseCase<Unit, NavDestination> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(params: Unit): Flow<NavDestination> {
        return combine(
            preferencesDataSource.activeProfileId,
            preferencesDataSource.pinEncryptedSecret,
            ::Pair
        )
            .flatMapLatest { (profileId, pinSecret) ->
                profileId?.let {
                    if (authenticationService.isProfileSignedIn(it) && pinSecret != null) flowOf(
                        NavDestination.Authenticate
                    )
                    else flowOf(NavDestination.Login)
                } ?: flowOf(NavDestination.Registration)
            }
    }

}