package com.vadlevente.bingebot.core.inject

import com.vadlevente.bingebot.core.delegates.AppCloserDelegate
import com.vadlevente.bingebot.core.delegates.AppCloserDelegateImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DelegateModule {

    @Singleton
    @Binds
    abstract fun bindAppCloserDelegate(
        impl: AppCloserDelegateImpl
    ): AppCloserDelegate


}