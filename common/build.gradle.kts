plugins {
    id("library-common")
}

android {
    namespace = "com.vadlevente.bingebot.common"
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Config.composeCompilerExtensionVersion
    }
}

dependencies{
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.resources))
    "implementation"(project(Modules.ui))

    "implementation"(Biometrics.biometrics)
    "implementation"(Ui.navigationUi)
    "implementation"(Compose.material)
    "implementation"(Compose.ui)
    "implementation"(Compose.preview)
    "implementation"(Compose.lifecycleCompose)
    "implementation"(Compose.navigation)
    "implementation"(Compose.coil)
    "implementation"(Compose.materialIcons)
    "implementation"(Hilt.hiltNavigation)
}