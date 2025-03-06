package com.vadlevente.bingebot.core.inject

import com.vadlevente.bingebot.core.data.service.AuthenticationService
import com.vadlevente.bingebot.core.data.service.AuthenticationServiceImpl
import com.vadlevente.bingebot.core.data.service.SecretService
import com.vadlevente.bingebot.core.data.service.SecretServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    @Singleton
    abstract fun bindsAuthenticationService(
        impl: AuthenticationServiceImpl
    ): AuthenticationService

    @Binds
    @Singleton
    abstract fun bindsSecretService(
        impl: SecretServiceImpl
    ): SecretService

}