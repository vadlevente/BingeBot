# =====================
# Keep everything needed for Retrofit + Kotlin + DataStore + Room
# =====================
-dontwarn com.google.auto.service.AutoService
-dontwarn org.junit.jupiter.api.extension.AfterAllCallback
-dontwarn org.junit.jupiter.api.extension.ParameterResolver
-dontwarn org.junit.jupiter.api.extension.TestInstancePostProcessor

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
