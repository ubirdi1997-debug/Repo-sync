package com.example.data.model

import androidx.compose.ui.graphics.Color

enum class MoodType(
    val title: String,
    val description: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val emoji: String,
    val waveFrequency: Float,
    val waveSpeed: Float
) {
    ZEN_CALM(
        title = "Zen Calm",
        description = "Minimal, graceful, meditative logic with clean abstractions",
        primaryColor = Color(0xFF38BDF8),
        secondaryColor = Color(0xFF2DD4BF),
        emoji = "🧘‍♂️",
        waveFrequency = 0.015f,
        waveSpeed = 0.6f
    ),
    HYPER_FLOW(
        title = "Hyper Flow",
        description = "High-velocity, reactive, event-driven state streams",
        primaryColor = Color(0xFFA855F7),
        secondaryColor = Color(0xFFEC4899),
        emoji = "⚡",
        waveFrequency = 0.035f,
        waveSpeed = 1.6f
    ),
    CYBERPUNK(
        title = "Cyberpunk",
        description = "Aggressive micro-optimizations, neon edges, robust concurrency",
        primaryColor = Color(0xFF22C55E),
        secondaryColor = Color(0xFF06B6D4),
        emoji = "👾",
        waveFrequency = 0.028f,
        waveSpeed = 1.3f
    ),
    MEDITATIVE_SLATE(
        title = "Monochrome",
        description = "Brutalist simplicity, zero fluff, deterministic control flow",
        primaryColor = Color(0xFF94A3B8),
        secondaryColor = Color(0xFFCBD5E1),
        emoji = "🖤",
        waveFrequency = 0.018f,
        waveSpeed = 0.8f
    ),
    CELESTIAL(
        title = "Celestial",
        description = "Dreamy, intuitive UI bindings with fluid spring animations",
        primaryColor = Color(0xFF818CF8),
        secondaryColor = Color(0xFFF472B6),
        emoji = "🌌",
        waveFrequency = 0.022f,
        waveSpeed = 1.0f
    )
}

data class LogicBlueprint(
    val title: String,
    val mood: MoodType,
    val architecture: String,
    val steps: List<String>,
    val stateMachineStates: List<String>,
    val generatedCode: String,
    val language: String = "Kotlin / Compose",
    val testCasesCount: Int = 3
)
