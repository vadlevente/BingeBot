
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class LibraryConventionPlugin: Plugin<Project> {
  override fun apply(project: Project) {
    project.plugins.apply("com.android.library")
    project.plugins.apply("org.jetbrains.kotlin.android")
    project.plugins.apply("kotlin-android")
    project.plugins.apply("kotlin-kapt")
    project.plugins.apply("kotlin-parcelize")
    project.plugins.apply("kotlinx-serialization")
    project.plugins.apply("dagger.hilt.android.plugin")

    val android = project.extensions.getByType<com.android.build.gradle.LibraryExtension>()
    android.apply {
      compileSdk = Config.compileSdkVersion

      defaultConfig {
        minSdk = Config.minSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-proguard-rules.pro")
        buildConfigField("String", "ACCESS_TOKEN", "\"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzZGQ2YWQzNjcxOTkzZTYwZWQ1MWMzMTI2NmIxZWMzNiIsIm5iZiI6MTcyMDE2MDQ2Ny4yNTQ4MTMsInN1YiI6IjVhZWRiMTc5YzNhMzY4MzQ3MDAwYTk1MSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.h9nnbkDhH_wvGjQzek7qtWzHMZ4lmmypkxyeV5kfEyU\"")
        buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3/\"")
      }

      compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
      }
    }
    project.dependencies.apply {
      add("implementation", Hilt.hilt)
      add("kapt", Hilt.hiltCompiler)
    }
  }
}