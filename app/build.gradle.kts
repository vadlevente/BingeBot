plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
}

android {
    namespace = "com.vadlevente.bingebot"
    defaultConfig {
        buildConfigField("String", "ACCESS_TOKEN", "\"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzZGQ2YWQzNjcxOTkzZTYwZWQ1MWMzMTI2NmIxZWMzNiIsIm5iZiI6MTcyMDE2MDQ2Ny4yNTQ4MTMsInN1YiI6IjVhZWRiMTc5YzNhMzY4MzQ3MDAwYTk1MSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.h9nnbkDhH_wvGjQzek7qtWzHMZ4lmmypkxyeV5kfEyU\"")
        buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3/\"")
    }
    signingConfigs {
        create("release") {
            storeFile = rootProject.file("keystore.jks")
            storePassword = "password"
            keyAlias = "key0"
            keyPassword = "password"
        }
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    bundle {
        language {
            enableSplit = false
        }
    }
    packaging {
        resources {
            excludes += listOf(
                "fonts/*.ttf",
                "fonts/*.otf",
                "fonts/*.ttc",
                "**/win32/**",
                "**/win64/**",
                "**/win32-x86/**",
                "**/win32-x86-64/**",
                "**/sqlite4java/**",
                "**/icu/**",
                "**/icudt*.dat",
                "**/icudtl.dat"
            )
        }
    }
}

dependencies {
    implementation(project(Modules.core))
    implementation(project(Modules.ui))
    implementation(project(Modules.splash))
    implementation(project(Modules.authentication))
    implementation(project(Modules.listAll))
    implementation(project(Modules.search))
    implementation(project(Modules.watchlist))
    implementation(project(Modules.details))
    implementation(project(Modules.resources))
    implementation(project(Modules.dashboard))
    implementation(project(Modules.bottomSheet))
    implementation(project(Modules.settings))

// Core
    implementation(Kotlin.kotlinStdLib)
    implementation(Kotlin.kotlinSerialization)

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
    implementation(Compose.materialIcons)
    implementation(Compose.coil)
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
    implementation(Networking.okhttpLogger)
    implementation(Networking.retrofitGsonConverter)

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
    implementation(Firebase.firebaseFirestore)

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
    testImplementation(Testing.kotlin)
    testImplementation(Testing.kotlinJunit)
    testImplementation(Concurrency.coroutinestest)
    testImplementation(Testing.turbine)
}

apply(plugin = Firebase.googleServicesPlugin)
apply(plugin = Firebase.crashlyticsPlugin)