package com.bingebot.splash.inject

import com.bingebot.splash.usecase.NavigateToStartScreen
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SplashModule {

    @Binds
    @Singleton
    abstract fun bindsNavigateToStartScreen(
        impl: NavigateToStartScreen
    ): NavigateToStartScreen

}