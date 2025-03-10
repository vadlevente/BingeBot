import org.gradle.kotlin.dsl.android

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "com.vadlevente.bingebot.list"
}

apply{
    from("$rootDir/compose-module.gradle")
}
dependencies{
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.ui))
    "implementation"(project(Modules.resources))
}