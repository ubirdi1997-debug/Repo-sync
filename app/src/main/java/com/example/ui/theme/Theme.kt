package com.example.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color

@Immutable
data class VibeExtraColors(
    val border: Color,
    val textMuted: Color,
    val surfaceVariant: Color,
    val waveColors: List<Color>
)

val LocalVibeExtraColors = compositionLocalOf {
    VibeExtraColors(
        border = Color(0xFF1E293B),
        textMuted = Color(0xFF94A3B8),
        surfaceVariant = Color(0xFF182238),
        waveColors = listOf(Color(0xFF38BDF8), Color(0xFF2DD4BF))
    )
}

@Composable
fun VibeTheme(
    preset: DesignPreset = DesignPreset.OBSIDIAN_NEO,
    darkTheme: Boolean = true,
    highContrast: Boolean = false,
    content: @Composable () -> Unit
) {
    val springSpec = spring<Color>(
        dampingRatio = 0.85f,
        stiffness = Spring.StiffnessMediumLow
    )

    // Smoothly animate each theme color token during dark <-> light transitions
    val targetBg = if (darkTheme) preset.darkBackground else preset.lightBackground
    val targetSurface = if (darkTheme) preset.darkSurface else preset.lightSurface
    val targetSurfaceVar = if (darkTheme) preset.darkSurfaceVariant else preset.lightSurfaceVariant
    val targetPrimary = if (darkTheme) preset.darkPrimary else preset.lightPrimary
    val targetSecondary = if (darkTheme) preset.darkSecondary else preset.lightSecondary
    val targetBorder = if (darkTheme) {
        if (highContrast) Color.White.copy(alpha = 0.4f) else preset.darkBorder
    } else {
        if (highContrast) Color.Black.copy(alpha = 0.4f) else preset.lightBorder
    }
    val targetText = if (darkTheme) preset.darkText else preset.lightText
    val targetTextMuted = if (darkTheme) preset.darkTextMuted else preset.lightTextMuted

    val animatedBg by animateColorAsState(targetValue = targetBg, animationSpec = springSpec, label = "bg")
    val animatedSurface by animateColorAsState(targetValue = targetSurface, animationSpec = springSpec, label = "surface")
    val animatedSurfaceVar by animateColorAsState(targetValue = targetSurfaceVar, animationSpec = springSpec, label = "surfaceVar")
    val animatedPrimary by animateColorAsState(targetValue = targetPrimary, animationSpec = springSpec, label = "primary")
    val animatedSecondary by animateColorAsState(targetValue = targetSecondary, animationSpec = springSpec, label = "secondary")
    val animatedBorder by animateColorAsState(targetValue = targetBorder, animationSpec = springSpec, label = "border")
    val animatedText by animateColorAsState(targetValue = targetText, animationSpec = springSpec, label = "text")
    val animatedTextMuted by animateColorAsState(targetValue = targetTextMuted, animationSpec = springSpec, label = "textMuted")

    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = animatedPrimary,
            secondary = animatedSecondary,
            background = animatedBg,
            surface = animatedSurface,
            onBackground = animatedText,
            onSurface = animatedText,
            onPrimary = if (darkTheme) Color.Black else Color.White,
            surfaceVariant = animatedSurfaceVar,
            outline = animatedBorder
        )
    } else {
        lightColorScheme(
            primary = animatedPrimary,
            secondary = animatedSecondary,
            background = animatedBg,
            surface = animatedSurface,
            onBackground = animatedText,
            onSurface = animatedText,
            onPrimary = Color.White,
            surfaceVariant = animatedSurfaceVar,
            outline = animatedBorder
        )
    }

    val extraColors = VibeExtraColors(
        border = animatedBorder,
        textMuted = animatedTextMuted,
        surfaceVariant = animatedSurfaceVar,
        waveColors = if (darkTheme) preset.waveColorsDark else preset.waveColorsLight
    )

    CompositionLocalProvider(LocalVibeExtraColors provides extraColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
