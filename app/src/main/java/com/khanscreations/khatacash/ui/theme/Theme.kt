package com.khanscreations.khatacash.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = FigmaGreenPrimary,
    onPrimary = FigmaSurfaceWhite,
    primaryContainer = FigmaGreenLight,
    onPrimaryContainer = FigmaGreenDark,
    secondary = FigmaAmberLight,
    onSecondary = FigmaTextPrimary,
    background = FigmaBackground,
    onBackground = FigmaTextPrimary,
    surface = FigmaSurfaceWhite,
    onSurface = FigmaTextPrimary,
    surfaceVariant = FigmaGreenSoftBg,
    onSurfaceVariant = FigmaTextMuted,
    outline = FigmaBorder
)

@Composable
fun KhataCashTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
