package com.example.ui.theme

import androidx.compose.ui.graphics.Color

enum class DesignPreset(
    val id: String,
    val displayName: String,
    val tagline: String,
    val darkBackground: Color,
    val darkSurface: Color,
    val darkSurfaceVariant: Color,
    val darkPrimary: Color,
    val darkSecondary: Color,
    val darkBorder: Color,
    val darkText: Color,
    val darkTextMuted: Color,
    
    val lightBackground: Color,
    val lightSurface: Color,
    val lightSurfaceVariant: Color,
    val lightPrimary: Color,
    val lightSecondary: Color,
    val lightBorder: Color,
    val lightText: Color,
    val lightTextMuted: Color,
    
    val waveColorsDark: List<Color>,
    val waveColorsLight: List<Color>
) {
    OBSIDIAN_NEO(
        id = "obsidian_neo",
        displayName = "Obsidian Neo",
        tagline = "Minimalist Cyber aesthetic • High-contrast electric accents",
        darkBackground = Color(0xFF070A0F),
        darkSurface = Color(0xFF0F1523),
        darkSurfaceVariant = Color(0xFF182238),
        darkPrimary = Color(0xFF38BDF8),
        darkSecondary = Color(0xFF2DD4BF),
        darkBorder = Color(0xFF1E293B),
        darkText = Color(0xFFF8FAFC),
        darkTextMuted = Color(0xFF94A3B8),

        lightBackground = Color(0xFFF8FAFC),
        lightSurface = Color(0xFFFFFFFF),
        lightSurfaceVariant = Color(0xFFF1F5F9),
        lightPrimary = Color(0xFF0284C7),
        lightSecondary = Color(0xFF0D9488),
        lightBorder = Color(0xFFE2E8F0),
        lightText = Color(0xFF0F172A),
        lightTextMuted = Color(0xFF64748B),

        waveColorsDark = listOf(Color(0xFF38BDF8), Color(0xFF2DD4BF), Color(0xFF6366F1)),
        waveColorsLight = listOf(Color(0x600284C7), Color(0x600D9488), Color(0x506366F1))
    ),

    LIQUID_SLATE(
        id = "liquid_slate",
        displayName = "Liquid Slate",
        tagline = "Pure Zen Monochrome • Brutalist precision & meditative flow",
        darkBackground = Color(0xFF09090B),
        darkSurface = Color(0xFF141417),
        darkSurfaceVariant = Color(0xFF202025),
        darkPrimary = Color(0xFFE4E4E7),
        darkSecondary = Color(0xFFA1A1AA),
        darkBorder = Color(0xFF27272A),
        darkText = Color(0xFFFAFAFA),
        darkTextMuted = Color(0xFF71717A),

        lightBackground = Color(0xFFFAFAFA),
        lightSurface = Color(0xFFFFFFFF),
        lightSurfaceVariant = Color(0xFFF4F4F5),
        lightPrimary = Color(0xFF18181B),
        lightSecondary = Color(0xFF52525B),
        lightBorder = Color(0xFFE4E4E7),
        lightText = Color(0xFF09090B),
        lightTextMuted = Color(0xFF71717A),

        waveColorsDark = listOf(Color(0xFF71717A), Color(0xFFA1A1AA), Color(0xFF52525B)),
        waveColorsLight = listOf(Color(0x50A1A1AA), Color(0x5071717A), Color(0x40D4D4D8))
    ),

    CELESTIAL_AURORA(
        id = "celestial_aurora",
        displayName = "Celestial Twilight",
        tagline = "Deep violet atmospheric glow • Organic spring fluid energy",
        darkBackground = Color(0xFF090717),
        darkSurface = Color(0xFF13102D),
        darkSurfaceVariant = Color(0xFF211D4B),
        darkPrimary = Color(0xFF818CF8),
        darkSecondary = Color(0xFFF472B6),
        darkBorder = Color(0xFF2E2963),
        darkText = Color(0xFFF5F3FF),
        darkTextMuted = Color(0xFFA5B4FC),

        lightBackground = Color(0xFFFBFBFE),
        lightSurface = Color(0xFFFFFFFF),
        lightSurfaceVariant = Color(0xFFF3F0FF),
        lightPrimary = Color(0xFF6366F1),
        lightSecondary = Color(0xFFDB2777),
        lightBorder = Color(0xFFE0E7FF),
        lightText = Color(0xFF1E1B4B),
        lightTextMuted = Color(0xFF6D28D9),

        waveColorsDark = listOf(Color(0xFF818CF8), Color(0xFFC084FC), Color(0xFFF472B6)),
        waveColorsLight = listOf(Color(0x606366F1), Color(0x60A855F7), Color(0x50EC4899))
    )
}
