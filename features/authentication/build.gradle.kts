import org.gradle.kotlin.dsl.android

plugins {
    id("feature-common")
}

android {
    namespace = "com.vadlevente.bingebot.authentication"
}

dependencies{
    "implementation"(Biometrics.biometrics)
}