import org.gradle.kotlin.dsl.android

plugins {
    id("com.android.library") 
    id("kotlin-android")
}

android {
    namespace = "com.vadlevente.bingebot.resources"
}

apply{
    from("$rootDir/compose-module.gradle")
}
dependencies{

}