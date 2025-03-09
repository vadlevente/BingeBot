@Suppress("MemberNameEqualsClassName")
object Hilt {
    // DI
    private const val hiltVersion = "2.45"
    private const val hiltNavigationVersion = "1.2.0-alpha01"
    private const val hiltCompilerVersion = "2.44"

    const val hilt = "com.google.dagger:hilt-android:$hiltVersion"
    const val hiltGradle = "com.google.dagger:hilt-android-gradle-plugin:$hiltVersion"
    const val hiltNavigation = "androidx.hilt:hilt-navigation-compose:$hiltNavigationVersion"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:$hiltCompilerVersion"
    const val hiltTest = "com.google.dagger:hilt-android-testing:$hiltVersion"
}
