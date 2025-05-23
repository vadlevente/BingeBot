
plugins {
    id("com.android.library") 
    id("kotlin-android")
    id("kotlinx-serialization")
}

android {
    namespace = "com.vadlevente.bingebot.core"
}

apply{
    from("$rootDir/compose-module.gradle")
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
    "implementation"(Concurrency.rxJava)
    "implementation"(Concurrency.rxAndroid)
    "implementation"(Concurrency.coroutinesRx)
    "implementation"(Data.room)
    "implementation"(Data.roomKtx)
    "implementation"(Data.roomRx)
    "implementation"(Networking.retrofit)
    "implementation"(Networking.okHttp)
    "implementation"(Networking.okhttpLogger)
    "implementation"(Networking.retrofitGsonConverter)
    "implementation"(Kotlin.kotlinSerialization)
    "kapt"(Data.roomCompiler)
    "implementation"(Data.dataStore)
    "implementation"(Data.cryptoDataStore)
    "implementation"(Utils.timber)
    "implementation"(Utils.gson)
    "implementation"(Biometrics.biometrics)
    // Firebase
    "implementation"(platform(Firebase.firebaseBoM))
    "implementation"(Firebase.firebaseCrashlytics)
    "implementation"(Firebase.firebaseAuth)
    "implementation"(Firebase.firebaseFirestore)
//    "implementation"(project(Modules.coreUi))
    "testImplementation"(Testing.kotlin)
    "androidTestImplementation"(Testing.kotlin)
    "androidTestImplementation"(Testing.turbine)
    "androidTestImplementation"(Networking.okHttp)
}