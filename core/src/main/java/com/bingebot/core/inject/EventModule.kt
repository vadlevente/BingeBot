package com.bingebot.core.inject

import com.bingebot.core.events.dialog.DialogEventChannel
import com.bingebot.core.events.dialog.DialogEventChannelImpl
import com.bingebot.core.events.navigation.NavigationEventChannel
import com.bingebot.core.events.navigation.NavigationEventChannelImpl
import com.bingebot.core.events.toast.ToastEventChannel
import com.bingebot.core.events.toast.ToastEventChannelImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EventModule {

    @Binds
    @Singleton
    abstract fun bindNavigationEventChannel(
        impl: NavigationEventChannelImpl
    ): NavigationEventChannel

    @Binds
    @Singleton
    abstract fun bindToastEventChannel(
        impl: ToastEventChannelImpl
    ): ToastEventChannel

    @Binds
    @Singleton
    abstract fun bindDialogEventChannel(
        impl: DialogEventChannelImpl
    ): DialogEventChannel

}