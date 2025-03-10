
plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "com.vadlevente.bingebot.bottomsheet"
}

apply{
    from("$rootDir/compose-module.gradle")
}

dependencies{
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.ui))
    "implementation"(project(Modules.resources))
}