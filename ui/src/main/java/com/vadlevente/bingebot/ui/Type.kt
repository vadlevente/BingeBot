package com.vadlevente.bingebot.ui

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

private val fontFamily = FontFamily.Default

val pageTitle = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 24.sp,
    letterSpacing = 0.6.sp,
    color = lightTextColor,
)

val errorLabel = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    letterSpacing = 0.6.sp,
    color = errorColor,
)

val link = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    letterSpacing = 0.6.sp,
    color = Color.Blue
)

val listItemTitle = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    letterSpacing = 0.6.sp,
    color = lightTextColor,
)

val listItemSubtitle = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 18.sp,
    letterSpacing = 0.6.sp,
    color = lightTextColor,
)

val listDescription = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 17.sp,
    letterSpacing = 0.6.sp,
    textAlign = TextAlign.Center,
    color = lightTextColor,
)

val bottomSheetAction = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 17.sp,
    letterSpacing = 0.6.sp,
    color = lightTextColor,
)

val dialogTitle = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 17.sp,
    letterSpacing = 0.6.sp,
    textAlign = TextAlign.Center,
    color = lightTextColor,
)

val dialogDescription = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 15.sp,
    letterSpacing = 0.6.sp,
    color = lightTextColor,
)

val outlinedButtonLabel = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 15.sp,
    letterSpacing = 0.6.sp,
    textAlign = TextAlign.Center,
    color = lightTextColor,
)

val buttonLabel = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 15.sp,
    letterSpacing = 0.6.sp,
    textAlign = TextAlign.Center,
    color = darkTextColor,
)

val selectedChipLabel = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 17.sp,
    letterSpacing = 0.6.sp,
    textAlign = TextAlign.Center,
    color = darkTextColor,
)

val unselectedChipLabel = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 17.sp,
    letterSpacing = 0.6.sp,
    textAlign = TextAlign.Center,
    color = lightTextColor,
)