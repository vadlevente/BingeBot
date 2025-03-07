package com.vadlevente.bingebot.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Define colors
val SoftTerracotta = Color(0xFFD88C65)
val DeepRust = Color(0xFF9C4221)
val GoldenSand = Color(0xFFF4A261)
val WarmBeige = Color(0xFFFAF3E0)
val CharcoalGray = Color(0xFF2E2E2E)
val DarkGray = Color(0xFF3B3B3B)
val MutedRed = Color(0xFFD32F2F)
val DesaturatedRed = Color(0xFFCF6679)
val PureWhite = Color(0xFFFFFFFF)

val LightInfo = Color(0xFF42A5F5) // Soft Blue
val DarkInfo = Color(0xFF90CAF9) // Lighter Blue
val LightWarning = Color(0xFFFFA726) // Warm Orange
val DarkWarning = Color(0xFFFFCC80) // Softer Orange
val LightHighlight = Color(0xFFF28C28) // Bright Amber
val DarkHighlight = Color(0xFFFFB74D) // Vivid Orange
val ShimmerBaseLight = Color(0xFFFCF8F3)       // Soft off-white with warmth
val ShimmerHighlightLight = Color(0xFFFCE1C5)  // Soft peachy beige
val ShimmerOverlayLight = Color(0xFFF0C9A0)    // Muted golden hue
val ShimmerBaseDark = Color(0xFF2E2E2E)       // Muted dark terracotta
val ShimmerHighlightDark = DarkGray  // Muted golden-brown glow
val ShimmerOverlayDark = Color(0xFF5A4035)    // Warm sandy beige

// Light theme colors
val LightColorScheme: ColorScheme = lightColorScheme(
    primary = SoftTerracotta,
    primaryContainer = DeepRust,
    secondary = GoldenSand,
    background = WarmBeige,
    surface = PureWhite,
    error = MutedRed,
    onPrimary = PureWhite,
    onSecondary = CharcoalGray,
    onBackground = CharcoalGray,
    onSurface = CharcoalGray
)

// Dark theme colors
val DarkColorScheme: ColorScheme = darkColorScheme(
    primary = GoldenSand,
    primaryContainer = SoftTerracotta,
    secondary = DeepRust,
    background = CharcoalGray,
    surface = DarkGray,
    error = DesaturatedRed,
    onPrimary = CharcoalGray,
    onSecondary = WarmBeige,
    onBackground = WarmBeige,
    onSurface = WarmBeige
)

@Immutable
data class CustomColorsPalette(
    val info: Color = Color.Unspecified,
    val warning: Color = Color.Unspecified,
    val highlight: Color = Color.Unspecified,
    val shimmerBase: Color = Color.Unspecified,
    val shimmerHighlight: Color = Color.Unspecified,
    val shimmerOverlay: Color = Color.Unspecified,
)

val LightCustomColorsPalette = CustomColorsPalette(
    info = LightInfo,
    warning = LightWarning,
    highlight = LightHighlight,
    shimmerBase = ShimmerBaseLight,
    shimmerHighlight = ShimmerHighlightLight,
    shimmerOverlay = ShimmerOverlayLight,
)

val DarkCustomColorsPalette = CustomColorsPalette(
    info = DarkInfo,
    warning = DarkWarning,
    highlight = DarkHighlight,
    shimmerBase = ShimmerBaseDark,
    shimmerHighlight = ShimmerHighlightDark,
    shimmerOverlay = ShimmerOverlayDark,
)

val LocalCustomColorsPalette = staticCompositionLocalOf { CustomColorsPalette() }

object BingeBotTheme {
    val colors: CustomColorsPalette
        @Composable
        @ReadOnlyComposable
        get() = LocalCustomColorsPalette.current
}

