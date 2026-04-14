package com.example.kindred.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    background = Color(0xFF0F0F0F),
    surface = Color(0xFF0F0F0F),
    onBackground = Color(0xFFF5F5F0),
    onSurface = Color(0xFFF5F5F0),
    primary = Color(0xFFF5F5F0),
    onPrimary = Color(0xFF0F0F0F)
)

private val LightColorScheme = lightColorScheme(
    background = Color(0xFFEED1A6),
    surface = Color(0xFFFAFAF8),
    onBackground = Color(0xFF0F0F0F),
    onSurface = Color(0xFF0F0F0F),
    primary = Color(0xFF0F0F0F),
    onPrimary = Color(0xFFFAFAF8)
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