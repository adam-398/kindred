package com.example.kindred.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Terracotta = Color(0xFFD67D61)
val DeepCharcoal = Color(0xFF1A1C1E)
val SoftSage = Color(0xFF8DA399)

private val DarkColorScheme = darkColorScheme(
    primary = Terracotta,
    onPrimary = Color.White,
    secondary = SoftSage,
    background = Color(0xFF1C1917),      // warm dark brown, not cold grey
    surface = Color(0xFF242120),          // slightly lighter warm brown
    onBackground = Color(0xFFEDE0D4),     // warm off-white
    onSurface = Color(0xFFEDE0D4),
    surfaceVariant = Color(0xFF3A3330),   // warm muted tone for cards
    onSurfaceVariant = Color(0xFFD0C4B8)
)

private val LightColorScheme = lightColorScheme(
    primary = Terracotta,
    primaryContainer = Color(0xFFFFD0B5),
    onPrimary = Color.White,
    secondary = SoftSage,
    background = Color(0xFFF5EDE3),       // soft warm white
    surface = Color(0xFFFAF4EE),          // slightly lighter warm white
    onBackground = DeepCharcoal,
    onSurface = DeepCharcoal,
    surfaceVariant = Color(0xFFEDE0D4),   // warm beige replacing the pink
    onSurfaceVariant = Color(0xFF5C4A3A)  // warm dark brown for secondary text
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
@Composable
fun MyChipColor() = FilterChipDefaults.filterChipColors(
    selectedContainerColor = Terracotta,
    selectedLabelColor = Color.White
)
