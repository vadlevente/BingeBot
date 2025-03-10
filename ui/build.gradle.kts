
plugins {
    id("com.android.library") 
    id("kotlin-android")
}

android {
    namespace = "com.vadlevente.bingebot.ui"
}

apply{
    from("$rootDir/compose-module.gradle")
}
dependencies{

}