apply{
    from("$rootDir/compose-module.gradle")
}
dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.ui))
    "implementation"(project(Modules.resources))
}