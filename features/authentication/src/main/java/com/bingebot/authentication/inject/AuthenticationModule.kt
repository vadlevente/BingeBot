package com.bingebot.authentication.inject

import com.bingebot.authentication.domain.usecase.LoginUseCase
import com.bingebot.authentication.domain.usecase.RegistrationUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthenticationModule {

    @Binds
    @Singleton
    abstract fun bindRegistrationUseCase(
        impl: RegistrationUseCase
    ): RegistrationUseCase

    @Binds
    @Singleton
    abstract fun bindLoginUseCase(
        impl: LoginUseCase
    ): LoginUseCase

}