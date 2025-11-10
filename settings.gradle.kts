pluginManagement {
//    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "BingeBot"
include(":app")
include(":ui")
include(":core")
include(":common")
include(":features:splash")
include(":features:authentication")
include(":features:listall")
include(":features:search")
include(":features:details")
include(":features:watchlist")
include(":resources")
include(":bottomsheet")
include(":features:dashboard")
include(":features:settings")
include(":features:demo")
