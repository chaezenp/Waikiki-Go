package com.waikikigo.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = WaikikiOceanBlue,
    secondary = SunsetCoral,
    tertiary = WaikikiSurfTeal,
    background = LightBackground,
    surface = LightSurface,
    onPrimary = LightSurface,
    onSecondary = LightSurface,
    onBackground = LightTextPrimary,
    onSurface = LightTextPrimary
)

private val DarkColorScheme = darkColorScheme(
    primary = WaikikiSurfTeal,
    secondary = SunsetCoral,
    tertiary = SandyGold,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = DarkBackground,
    onSecondary = DarkSurface,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary
)

@Composable
fun WaikikiGoTheme(
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
