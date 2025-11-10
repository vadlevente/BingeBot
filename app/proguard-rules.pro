# =====================
# Keep everything needed for Retrofit + Kotlin + DataStore + Room
# =====================
-dontwarn com.google.auto.service.AutoService
-dontwarn org.junit.jupiter.api.extension.AfterAllCallback
-dontwarn org.junit.jupiter.api.extension.ParameterResolver
-dontwarn org.junit.jupiter.api.extension.TestInstancePostProcessor
-dontwarn com.vadlevente.bingebot.app.Hilt_BingeBotApp
-dontwarn com.vadlevente.bingebot.app.Hilt_MainActivity

-keepattributes Signature, Exceptions, InnerClasses, EnclosingMethod, RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations, KotlinMetadata

# =====================
# Core models & DTOs
# =====================
-keep class com.vadlevente.bingebot.core.model.** { *; }
-keepclassmembers class com.vadlevente.bingebot.core.model.** {
    @com.google.gson.annotations.SerializedName <fields>;
}

# =====================
# Retrofit API interfaces & proxies
# =====================
-keep interface com.vadlevente.bingebot.core.data.api.** { *; }
-keepclassmembers class * {
    @retrofit2.http.* <methods>;
}
-keep class retrofit2.$Proxy* { *; }

# =====================
# Room & DataStore
# =====================
-keep class com.vadlevente.bingebot.core.data.local.datastore.** { *; }
-keep class androidx.room.** { *; }
-keepclassmembers class * { @androidx.room.* <methods>; }

# =====================
# Kotlin coroutines
# =====================
-keep class kotlin.coroutines.** { *; }
-keep class kotlinx.coroutines.** { *; }

# Activities, Fragments, and ViewModels annotated with @AndroidEntryPoint
-keep class **_HiltModules_* { *; }
-keep class * extends dagger.hilt.android.internal.lifecycle.HiltViewModelFactory$ViewModelFactoriesEntryPoint { *; }

-keep class dagger.hilt.internal.generated.** { *; }
-keep class * implements dagger.hilt.internal.GeneratedComponent { *; }
-keep class * implements dagger.hilt.internal.GeneratedEntryPoint { *; }
-keep class * extends dagger.hilt.android.internal.managers.HiltWrapper_Activity { *; }
-keep class * extends dagger.hilt.android.internal.managers.HiltWrapper_Application { *; }

-keep class com.vadlevente.bingebot.app.Hilt_BingeBotApp { *; }
-keep class com.vadlevente.bingebot.app.BingeBotApp { *; }
-keep class com.vadlevente.bingebot.app.Hilt_MainActivity { *; }
-keep class com.vadlevente.bingebot.app.MainActivity { *; }
