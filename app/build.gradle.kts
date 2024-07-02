plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.vadlevente.bingebot"

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("release") {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Config.composeCompilerExtensionVersion
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    hilt {
        enableAggregatingTask = true
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        composeOptions {
            kotlinCompilerExtensionVersion = Config.composeCompilerExtensionVersion
        }
    }
}

dependencies {
    implementation(project(Modules.core))
    implementation(project(Modules.ui))
    implementation(project(Modules.splash))
    implementation(project(Modules.authentication))
    implementation(project(Modules.list))
    implementation(project(Modules.search))

// Core
    implementation(Kotlin.kotlinStdLib)

// UI
    implementation(Ui.androidxCore)
    implementation(Ui.appCompat)
    implementation(Ui.material)
    implementation(Ui.constraintLayout)
    implementation(Ui.glide)
    implementation(Ui.lifecycleViewModel)
    implementation(Ui.lifecycleLiveData)
    implementation(Ui.lifecycleRuntime)
    implementation(Ui.lifecycleProcess)

// Compose
    implementation(Compose.material)
    implementation(Compose.preview)
    implementation(Compose.viewModel)
    implementation(Compose.appcompat)
    implementation(Compose.navigation)
    implementation(Compose.constraintLayout)
    implementation(Compose.pager)
    debugImplementation(Compose.tooling)
    debugImplementation(Compose.testManifest)

// Navigation
    implementation(Ui.navigationUi)

    // Data
    implementation(Networking.retrofit)
    implementation(Data.dataStore)
    implementation(Data.cryptoDataStore)
    implementation(Data.room)
    implementation(Data.roomKtx)
    implementation(Data.kotlinSerialization)
    implementation(Networking.okhttpLogger)

// LifeCycleCompose
    implementation(Compose.lifecycleCompose)

// Coroutines
    implementation(Concurrency.coroutines)
    implementation(Concurrency.coroutinesRx)

    // Utils
    coreLibraryDesugaring(Utils.coreLibraryDesugaring)
    implementation(Utils.timber)
    implementation(Utils.gson)

// Firebase
    implementation(platform(Firebase.firebaseBoM))
    implementation(Firebase.firebaseCrashlytics)
    implementation(Firebase.firebaseAuth)

// Kapt
    kapt(Data.roomCompiler)
    kapt(Hilt.hiltCompiler)

    implementation(Hilt.hilt)
    implementation(Hilt.hiltNavigation)

// TEST
    kaptTest(Hilt.hiltCompiler)
    testImplementation(Hilt.hiltTest)
    testImplementation(Testing.junit4)
    testImplementation(Testing.composeUiTest)
    testImplementation(Testing.testExtension)
    testImplementation(Testing.testExtensionKtx)
    testImplementation(Testing.espresso)
    testImplementation(Testing.espressoContrib)
    testImplementation(Testing.androidTestCoreKtx)
    testImplementation(Testing.androidArchTest)
    testImplementation(Testing.mockk)
    testImplementation(Testing.robolectric)
    testImplementation(Networking.okhttpTest)
    testImplementation(Testing.kotlin)
    testImplementation(Testing.kotlinJunit)
    testImplementation(Concurrency.coroutinestest)
    testImplementation(Testing.turbine)
}

apply(plugin = Firebase.googleServicesPlugin)
apply(plugin = Firebase.crashlyticsPlugin)