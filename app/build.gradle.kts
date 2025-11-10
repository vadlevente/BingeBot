plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    namespace = "com.vadlevente.bingebot"
    compileSdk = Config.compileSdkVersion
    defaultConfig {
        minSdk = Config.minSdkVersion
        versionCode = Config.versionCode
        versionName = Config.versionName
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Config.composeCompilerExtensionVersion
    }
    hilt {
        enableAggregatingTask = false
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
                "**/icudtl.dat",
                "META-INF/LICENSE*",
                "META-INF/NOTICE*",
                "META-INF/*.kotlin_module",
                "META-INF/*.version",
                "META-INF/**/INDEX.LIST",
                "META-INF/native/**",
                "native/**",
                "**/mac/**",
                "**/osx/**",
                "**/*.dylib",   // Mac binaries
                "**/*.dll",     // Windows binaries, just in case
                "**/linux/**",
                "**/*.so",      // If any desktop .so is sneaking in
                "**/*.jnilib",
            )
        }
        jniLibs {
            // Remove desktop native libs (not used on Android)
            excludes += setOf(
                "**/linux/**",
                "**/unix/**",
                "**/mac/**",
                "**/darwin/**",
                "**/osx/**",
                "**/win32/**",
                "**/win64/**"
            )
        }
    }
}

dependencies {
    implementation(project(Modules.core))
    implementation(project(Modules.ui))
    implementation(project(Modules.resources))
    implementation(project(Modules.common))
    implementation(project(Modules.splash))
    implementation(project(Modules.authentication))
    implementation(project(Modules.search))
    implementation(project(Modules.watchlist))
    implementation(project(Modules.details))
    implementation(project(Modules.resources))
    implementation(project(Modules.dashboard))
    implementation(project(Modules.bottomSheet))
    implementation(project(Modules.listAll))
    implementation(project(Modules.settings))

// Core
    implementation(Kotlin.kotlinStdLib)
    implementation(Kotlin.kotlinSerialization)

// LifeCycleCompose
    implementation(Compose.lifecycleCompose)
    implementation(Compose.material)
    implementation(Compose.viewModel)
    implementation(Compose.appcompat)
    implementation(Hilt.hiltNavigation)
// Coroutines
    implementation(Concurrency.coroutines)
    implementation(Concurrency.coroutinesRx)

    // Utils
    coreLibraryDesugaring(Utils.coreLibraryDesugaring)
    implementation(Utils.timber)
    implementation(Utils.gson)
    implementation(Hilt.hilt)
    kapt(Hilt.hiltCompiler)
    implementation(Networking.okHttp)
// Firebase
    implementation(platform(Firebase.firebaseBoM))
    implementation(Firebase.firebaseCrashlytics)
    implementation(Firebase.firebaseAuth)
    implementation(Firebase.firebaseFirestore)

// Kapt
    kapt(Data.roomCompiler)

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