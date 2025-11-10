
plugins {
    id("library-common")
    id("kotlin-android")
    id("kotlinx-serialization")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

android {
    namespace = "com.vadlevente.bingebot.core"
    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }
}

dependencies {
    "implementation"(project(Modules.ui))
    "implementation"(project(Modules.resources))
    "implementation"(Testing.junit4)
    "implementation"(Testing.composeUiTest)
    "implementation"(Testing.mockk)
    "implementation"(Testing.testRunner)
    "implementation"(Hilt.hiltTest)
    "implementation"(Testing.robolectric)
    "implementation"(Testing.navigationTest)
    "api"(Concurrency.rxJava)
    "api"(Concurrency.rxAndroid)
    "api"(Concurrency.coroutinesRx)
    "api"(Data.room)
    "api"(Data.roomKtx)
    "api"(Data.roomRx)
    "api"(Networking.retrofit)
    "api"(Networking.okHttp)
    "api"(Networking.okhttpLogger)
    "api"(Networking.retrofitGsonConverter)
    "api"(Kotlin.kotlinSerialization)
    "kapt"(Data.roomCompiler)
    "api"(Data.dataStore)
    "api"(Data.cryptoDataStore)
    "api"(Utils.timber)
    "api"(Utils.gson)
    "api"(Biometrics.biometrics)
    "api"(Ui.appCompat)
    "api"(Ui.androidxCore)

    // Firebase
    "implementation"(platform(Firebase.firebaseBoM))
    "implementation"(Firebase.firebaseCrashlytics)
    "implementation"(Firebase.firebaseAuth)
    "implementation"(Firebase.firebaseFirestore)
    "testImplementation"(Testing.kotlin)
    "androidTestImplementation"(Testing.kotlin)
    "androidTestImplementation"(Testing.turbine)
    "androidTestImplementation"(Networking.okHttp)

    "api"(Hilt.hilt)
    "kapt"(Hilt.hiltCompiler)
}