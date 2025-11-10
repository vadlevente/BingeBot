plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    // Android Gradle Plugin for LibraryExtension, AppExtension, etc.
    implementation("com.android.tools.build:gradle:8.7.3")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10")

    // Kotlin stdlib for plugin code
    implementation(kotlin("stdlib", "1.9.10"))
}

gradlePlugin {
    plugins {
        create("library-common") {
            id = "library-common"
            implementationClass = "LibraryConventionPlugin"
        }
        create("feature-common") {
            id = "feature-common"
            implementationClass = "FeatureUiConventionPlugin"
        }
    }
}
