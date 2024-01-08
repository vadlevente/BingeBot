package com.bingebot.core.inject

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import dagger.Provides
import io.github.osipxd.security.crypto.createEncrypted
import javax.inject.Singleton


object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStore(context: Context): DataStore<Preferences> = PreferenceDataStoreFactory.createEncrypted {
        EncryptedFile.Builder(
            // The file should have extension .preferences_pb
            context.dataStoreFile("encrypted.preferences_pb"),
            context,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
    }
}