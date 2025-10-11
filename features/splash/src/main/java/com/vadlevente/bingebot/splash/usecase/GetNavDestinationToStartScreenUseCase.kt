package com.vadlevente.bingebot.splash.usecase

import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.data.service.AuthenticationService
import com.vadlevente.bingebot.core.model.NavDestination.TopNavDestination
import com.vadlevente.bingebot.core.usecase.BaseUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetNavDestinationToStartScreenUseCase @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val preferencesDataSource: PreferencesDataSource,
) : BaseUseCase<Unit, TopNavDestination> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(params: Unit): Flow<TopNavDestination> {
        return preferencesDataSource.activeProfileId
            .flatMapLatest { profileId ->
                profileId?.let {
                    if (authenticationService.isProfileSignedIn(it)) flowOf(TopNavDestination.AuthenticatedScreens)
                    else flowOf(TopNavDestination.Onboarding)
                } ?: flowOf(TopNavDestination.Onboarding)
            }
    }

}