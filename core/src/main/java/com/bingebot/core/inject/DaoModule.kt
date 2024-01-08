package com.bingebot.core.inject

import com.bingebot.core.data.local.ActiveProfileIdDao
import com.bingebot.core.data.local.ActiveProfileIdDaoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DaoModule {

    @Binds
    @Singleton
    abstract fun bindActiveProfileIdDao(impl: ActiveProfileIdDaoImpl): ActiveProfileIdDao

}