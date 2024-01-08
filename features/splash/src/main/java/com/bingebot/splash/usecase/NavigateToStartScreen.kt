package com.bingebot.splash.usecase

import com.bingebot.core.data.authentication.AuthenticationService
import javax.inject.Inject

class NavigateToStartScreen @Inject constructor(
    private val authenticationService: AuthenticationService,
    ) {

    operator suspend fun invoke() {

    }

}