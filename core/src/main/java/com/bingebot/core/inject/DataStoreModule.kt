package com.bingebot.core.inject

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.bingebot.core.data.local.datastore.PreferencesDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.osipxd.security.crypto.createEncrypted
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreModule {

    companion object {
        @Provides
        @Singleton
        fun provideDataStore(context: Context): DataStore<Preferences> =
            PreferenceDataStoreFactory.createEncrypted {
                EncryptedFile.Builder(
                    context.dataStoreFile("encrypted.preferences_pb"),
                    context,
                    MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                    EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
                ).build()
            }
    }

    @Binds
    @Singleton
    abstract fun bindPreferencesDataSource(impl: PreferencesDataSource): PreferencesDataSource
}