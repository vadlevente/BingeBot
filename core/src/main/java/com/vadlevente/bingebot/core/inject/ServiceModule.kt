package com.vadlevente.bingebot.core.inject

import com.vadlevente.bingebot.core.data.service.AESService
import com.vadlevente.bingebot.core.data.service.AESServiceImpl
import com.vadlevente.bingebot.core.data.service.AuthenticationService
import com.vadlevente.bingebot.core.data.service.AuthenticationServiceImpl
import com.vadlevente.bingebot.core.data.service.BiometricsService
import com.vadlevente.bingebot.core.data.service.BiometricsServiceImpl
import com.vadlevente.bingebot.core.data.service.CryptographyService
import com.vadlevente.bingebot.core.data.service.CryptographyServiceImpl
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

    @Binds
    @Singleton
    abstract fun bindsAESService(
        impl: AESServiceImpl
    ): AESService

    @Binds
    @Singleton
    abstract fun bindsCryptographyService(
        impl: CryptographyServiceImpl
    ): CryptographyService

    @Binds
    @Singleton
    abstract fun bindsBiometricsService(
        impl: BiometricsServiceImpl
    ): BiometricsService

}