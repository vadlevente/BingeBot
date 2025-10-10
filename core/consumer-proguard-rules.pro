# =====================
# Kotlin metadata & reflection info
# =====================
-keepattributes Signature, *Annotation*, Exceptions, InnerClasses, EnclosingMethod, RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations, KotlinMetadata

# Keep @kotlin.Metadata for all core classes (needed for Retrofit + Flow)
-keep class com.vadlevente.bingebot.core.** {
    @kotlin.Metadata *;
}

# Keep generic signatures for Item hierarchy to prevent ClassCastException in release
-keep,allowobfuscation class com.vadlevente.bingebot.core.model.Item { @kotlin.Metadata *; }
-keep,allowobfuscation class com.vadlevente.bingebot.core.model.Item$Movie { @kotlin.Metadata *; }
-keep,allowobfuscation class com.vadlevente.bingebot.core.model.Item$Tv { @kotlin.Metadata *; }

# Keep Genre & its fields for Gson mapping
-keep class com.vadlevente.bingebot.core.model.Genre { *; }
-keepclassmembers class com.vadlevente.bingebot.core.model.Genre {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep Genre factories
-keep interface com.vadlevente.bingebot.core.model.GenreFactory { *; }
-keep class com.vadlevente.bingebot.core.model.GenreFactory$MovieGenreFactory { *; }
-keep class com.vadlevente.bingebot.core.model.GenreFactory$TvGenreFactory { *; }

# Keep Watchlist factories
-keep interface com.vadlevente.bingebot.core.model.WatchListFactory { *; }
-keep class com.vadlevente.bingebot.core.model.WatchListFactory$MovieWatchListFactory { *; }
-keep class com.vadlevente.bingebot.core.model.WatchListFactory$MovieWatchListFactory { *; }

# =====================
# Retrofit suspend methods
# =====================
-keepclassmembers class * {
    @retrofit2.http.* <methods>;
}

# =====================
# API interfaces
# =====================
-keep interface com.vadlevente.bingebot.core.data.api.** { *; }
-keep class com.vadlevente.bingebot.core.data.api.** { *; }

# =====================
# Remote data sources (Retrofit proxies)
# =====================
-keep class com.vadlevente.bingebot.core.data.remote.** { *; }

# =====================
# Repository implementations (Hilt bindings)
# =====================
-keep class com.vadlevente.bingebot.core.data.repository.** { *; }

# =====================
# Models (Gson serialization)
# =====================
-keep class com.vadlevente.bingebot.core.model.** { *; }

# Keep all DAOs (Room generated implementations rely on reflection/class lookups)
-keep class com.vadlevente.bingebot.core.data.dao.** { *; }

# Keep Room database models and generic signatures
-keep class androidx.room.** { *; }
-keepclassmembers class * {
    @androidx.room.* <methods>;
}

# =====================
# Kotlin coroutines
# =====================
-keep class kotlin.coroutines.** { *; }
-keep class kotlinx.coroutines.** { *; }

# Keep Retrofit interface proxies (important for dynamic proxy generation)
-keep class retrofit2.$Proxy* { *; }

# Keep all models and DTOs (Gson + Retrofit + R8 safe)
-keep class com.vadlevente.bingebot.core.model.** { *; }
-keepclassmembers class com.vadlevente.bingebot.core.model.** { <fields>; }

-keep class com.vadlevente.bingebot.core.model.dto.** { *; }
-keepclassmembers class com.vadlevente.bingebot.core.model.dto.** { <fields>; }

# Keep factories (to prevent inlining issues)
-keep class com.vadlevente.bingebot.core.model.**Factory { *; }

-keep class com.vadlevente.bingebot.core.model.GenreFactory {
    *;
}

# And explicitly keep sealed generic implementations
-keep class com.vadlevente.bingebot.core.model.GenreFactory$MovieGenreFactory { *; }
-keep class com.vadlevente.bingebot.core.model.GenreFactory$TvGenreFactory { *; }

# Keep enum ItemType intact (it participates in Genre copy)
-keep enum com.vadlevente.bingebot.core.model.ItemType { *; }

# Keep Retrofit DTO signature with generic info (critical!)
-keepclassmembers class com.vadlevente.bingebot.core.model.dto.GenresResponseDto {
    java.util.List genres;
}

-keep class com.google.gson.reflect.TypeToken {
    <fields>;
    <methods>;
}

# Preserve generic signature of TypeToken usage in your class
-keepclassmembers class com.vadlevente.bingebot.core.data.local.db.DbTypeConverters {
    <methods>;
}