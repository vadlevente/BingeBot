pluginManagement {
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
include(":features:splash")
include(":features:authentication")
include(":features:listall")
include(":features:search")
include(":features:moviedetails")
include(":features:watchlist")
include(":resources")
include(":bottomsheet")
include(":features:dashboard")
