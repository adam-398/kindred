package com.example.kindred.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


val Terracotta = Color(0xFFD67D61)
val WarmParchment = Color(0xFF000000)
val DeepCharcoal = Color(0xFF1A1C1E)
val SoftSage = Color(0xFF8DA399)
val MutedBerry = Color(0xFF945D60)
private val DarkColorScheme = darkColorScheme(
    primary = Terracotta,
    onPrimary = Color.White,
    secondary = SoftSage,
    background = DeepCharcoal,
    surface = Color(0xFF252729),
    onBackground = Color(0xFFE2E2E6),
    onSurface = Color(0xFFE2E2E6),
    surfaceVariant = Color(0xFF44474A)
)

private val LightColorScheme = lightColorScheme(
    primary = Terracotta,
    onPrimary = Color.White,
    secondary = SoftSage,
    background = Color(0xFFEFDFC5),
    surface = Color.White,
    onBackground = DeepCharcoal,
    onSurface = DeepCharcoal,
    surfaceVariant = Color(0xFFEAADBF)
)

@Composable
fun KindredTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}