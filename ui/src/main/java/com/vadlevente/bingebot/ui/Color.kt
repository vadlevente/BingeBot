package com.vadlevente.bingebot.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

//val darkGray = Color.DarkGray
//val backgroundColor = Color(0xFF1B1B1B)
//val onBackgroundColor = Color(0xFF5E5E5E)
//val onBackgroundColorFocused = Color(0xFF7A7A7A)
//val toolbarColor = Color(0xFF363636)
//val cardColor = Color(0xFF292929)
//val darkCardColor = Color(0xFF1F1E1E)
//val lightTextColor = Color(0xFFC0C0C0)
//val darkTextColor = Color(0xFF424242)
//val lightNavItemColor = Color(0xFFC0C0C0)
//val darkNavItemColor = Color(0xFF555555)
//val infoColor = Color(0xFF038603)
//val warningColor = Color(0xFFA0CC27)
//val errorColor = Color(0xFFC90000)
//val progressColor = Color(0xFF12B612)
//val white = Color.White

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
)

val LightCustomColorsPalette = CustomColorsPalette(
    info = LightInfo,
    warning = LightWarning,
    highlight = LightHighlight,
)

val DarkCustomColorsPalette = CustomColorsPalette(
    info = DarkInfo,
    warning = DarkWarning,
    highlight = DarkHighlight,
)

val LocalCustomColorsPalette = staticCompositionLocalOf { CustomColorsPalette() }

object BingeBotTheme {
    val colors: CustomColorsPalette
        @Composable
        @ReadOnlyComposable
        get() = LocalCustomColorsPalette.current
}

