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
    id("com.google.dagger.hilt.android") version "2.50" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10" apply false
}

task("clean") {
    delete(project.buildDir)
}