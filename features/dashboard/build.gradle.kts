import org.gradle.kotlin.dsl.android

plugins {
    id("feature-common")
}

android {
    namespace = "com.vadlevente.bingebot.dashboard"
}

dependencies{
    "implementation"(project(Modules.listAll))
    "implementation"(project(Modules.settings))
}