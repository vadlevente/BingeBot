
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class FeatureUiConventionPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    project.plugins.apply("library-common")

    val android = project.extensions.getByType<com.android.build.gradle.LibraryExtension>()
    android.apply {
      buildFeatures {
        compose = true
      }
      composeOptions {
        kotlinCompilerExtensionVersion = Config.composeCompilerExtensionVersion
      }
    }

    // Dependencies
    project.dependencies.apply {
      add("implementation", project.project(Modules.core))
      add("implementation", project.project(Modules.ui))
      add("implementation", project.project(Modules.resources))
      add("implementation", project.project(Modules.common))

      add("implementation", Compose.material)
      add("implementation", Compose.preview)
      add("implementation", Compose.viewModel)
      add("implementation", Compose.appcompat)
      add("implementation", Compose.materialIcons)
      add("implementation", Compose.coil)
      add("implementation", Compose.navigation)
      add("debugImplementation", Compose.tooling)
      add("debugImplementation", Compose.testManifest)

      add("implementation", Ui.androidxCore)
      add("implementation", Ui.appCompat)
      add("implementation", Ui.material)
      add("implementation", Ui.constraintLayout)
      add("implementation", Ui.glide)
      add("implementation", Ui.lifecycleViewModel)
      add("implementation", Ui.lifecycleLiveData)
      add("implementation", Ui.lifecycleRuntime)
      add("implementation", Ui.lifecycleProcess)
      add("implementation", Compose.constraintLayout)
      add("implementation", Compose.pager)
      add("implementation", Ui.navigationUi)
      add("implementation", Compose.lifecycleCompose)
      add("implementation", Hilt.hiltNavigation)
    }
  }
}
