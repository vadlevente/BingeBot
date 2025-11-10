import org.gradle.kotlin.dsl.android

plugins {
    id("library-common")
}

android {
    namespace = "com.vadlevente.bingebot.ui"
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Config.composeCompilerExtensionVersion
    }
}

dependencies{
    "implementation"(project(Modules.resources))

    "implementation"(Compose.material)
    "implementation"(Compose.ui)
    "implementation"(Ui.androidxCore)
}