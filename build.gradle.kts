buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.7.3")
        classpath(Kotlin.kotlinPlugin)
        classpath(Hilt.hiltGradle)
        classpath(Firebase.googleServices)
        classpath(Firebase.crashlytics)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    id("com.android.application") version "8.7.3" apply false
    id("com.android.library") version "8.7.3" apply false
    kotlin("android") version "1.9.10" apply false
    id("com.google.dagger.hilt.android") version "2.45" apply false
    kotlin("jvm") version "1.9.10" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10" apply false
}

task("clean") {
    delete(project.buildDir)
}

fun com.android.build.gradle.BaseExtension.baseConfig() {
    compileSdkVersion(Config.compileSdkVersion)
    defaultConfig.apply {
        minSdk = Config.minSdkVersion
        targetSdk = Config.compileSdkVersion
        versionCode = Config.versionCode
        versionName = Config.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

//        buildConfigField("String", "BASE_URL", ProjectConfig.baseUrl)
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = true
        }
    }

    testOptions.unitTests.isIncludeAndroidResources = true

    compileOptions.apply {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
}

fun PluginContainer.applyBaseConfig(project: Project) {
    whenPluginAdded {
        when (this) {
            is com.android.build.gradle.AppPlugin -> {
                project.extensions
                    .getByType<com.android.build.gradle.AppExtension>()
                    .apply {
                        baseConfig()
                    }
            }
            is com.android.build.gradle.LibraryPlugin -> {
                project.extensions
                    .getByType<com.android.build.gradle.LibraryExtension>()
                    .apply {
                        baseConfig()
                    }
            }
        }
    }
}

subprojects {
    project.plugins.applyBaseConfig(project)
}