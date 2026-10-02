package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.data.model.MoodType
import kotlin.math.sin

data class CanvasRipple(
    val center: Offset,
    val birthTime: Long
)

@Composable
fun FluidBackgroundCanvas(
    modifier: Modifier = Modifier,
    mood: MoodType = MoodType.ZEN_CALM,
    waveColors: List<Color>,
    isDark: Boolean = true,
    reducedMotion: Boolean = false,
    speedMultiplier: Float = 1.0f,
    userPrompt: String = "",
    isGenerating: Boolean = false
) {
    // Framer-Motion style spring interpolation for wave parameters
    val springPhysics = spring<Float>(
        dampingRatio = 0.75f,
        stiffness = Spring.StiffnessLow
    )

    // User prompt reactivity: more energetic waves with longer prompts
    val promptEnergyFactor = (userPrompt.length / 120f).coerceIn(0f, 0.6f)
    val generatingBoost = if (isGenerating) 0.5f else 0f

    val targetFrequency = (mood.waveFrequency + promptEnergyFactor * 0.01f).coerceIn(0.01f, 0.05f)
    val targetSpeed = (mood.waveSpeed * speedMultiplier + generatingBoost).coerceIn(0.4f, 3.0f)
    val targetAmplitude = if (isGenerating) 64f else (36f + promptEnergyFactor * 24f)

    val animatedFrequency by animateFloatAsState(targetValue = targetFrequency, animationSpec = springPhysics, label = "wave_freq")
    val animatedSpeed by animateFloatAsState(targetValue = targetSpeed, animationSpec = springPhysics, label = "wave_speed")
    val animatedAmplitude by animateFloatAsState(targetValue = targetAmplitude, animationSpec = springPhysics, label = "wave_amp")

    // Spring interpolation for wave colors
    val colorSpring = spring<Color>(dampingRatio = 0.8f, stiffness = Spring.StiffnessMediumLow)
    val animatedColor1 by animateColorAsState(targetValue = waveColors.getOrElse(0) { Color(0xFF38BDF8) }, animationSpec = colorSpring, label = "c1")
    val animatedColor2 by animateColorAsState(targetValue = waveColors.getOrElse(1) { Color(0xFF2DD4BF) }, animationSpec = colorSpring, label = "c2")
    val animatedColor3 by animateColorAsState(targetValue = waveColors.getOrElse(2) { Color(0xFF818CF8) }, animationSpec = colorSpring, label = "c3")

    val infiniteTransition = rememberInfiniteTransition(label = "fluid_motion_loop")

    val durationMs = if (reducedMotion) 60000 else (10000 / animatedSpeed).toInt().coerceIn(2500, 20000)

    val timePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (reducedMotion) 0.05f else (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMs, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    val ripples = remember { mutableStateListOf<CanvasRipple>() }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(reducedMotion) {
                if (!reducedMotion) {
                    detectTapGestures { tapOffset ->
                        if (ripples.size > 5) ripples.removeAt(0)
                        ripples.add(CanvasRipple(tapOffset, System.currentTimeMillis()))
                    }
                }
            }
    ) {
        val width = size.width
        val height = size.height

        if (width <= 0f || height <= 0f) return@Canvas

        // Static graceful background in reduced motion
        if (reducedMotion) {
            val calmBrush = Brush.verticalGradient(
                colors = listOf(
                    animatedColor1.copy(alpha = if (isDark) 0.06f else 0.04f),
                    animatedColor2.copy(alpha = if (isDark) 0.03f else 0.02f)
                )
            )
            drawRect(brush = calmBrush)
            return@Canvas
        }

        // 1. Draw floating luminous energy orbs
        val orbCenters = listOf(
            Offset(width * 0.2f + sin(timePhase) * 35f, height * 0.25f + sin(timePhase + 1f) * 25f),
            Offset(width * 0.82f + sin(timePhase * 0.8f + 2f) * 45f, height * 0.35f + sin(timePhase * 0.9f) * 30f),
            Offset(width * 0.32f + sin(timePhase * 1.1f + 3f) * 40f, height * 0.72f + sin(timePhase * 0.7f) * 30f),
            Offset(width * 0.75f + sin(timePhase * 0.6f + 4f) * 30f, height * 0.82f + sin(timePhase * 1.2f) * 25f)
        )

        val orbColors = listOf(animatedColor1, animatedColor2, animatedColor3, animatedColor1)
        val orbRadii = listOf(140f, 180f, 210f, 150f)

        orbCenters.forEachIndexed { i, center ->
            val color = orbColors[i]
            val radius = orbRadii[i] * (1f + if (isGenerating) 0.25f else 0f)
            val brush = Brush.radialGradient(
                colors = listOf(
                    color.copy(alpha = if (isDark) 0.16f else 0.09f),
                    color.copy(alpha = 0f)
                ),
                center = center,
                radius = radius
            )
            drawCircle(brush = brush, radius = radius, center = center)
        }

        // 2. Draw organic fluid wave ribbons
        drawWaveRibbon(
            width = width,
            height = height,
            baseY = height * 0.44f,
            amplitude = animatedAmplitude * 0.8f,
            wavelength = width * 1.05f,
            phase = timePhase,
            color = animatedColor1.copy(alpha = if (isDark) 0.13f else 0.08f),
            fillToBottom = false
        )

        drawWaveRibbon(
            width = width,
            height = height,
            baseY = height * 0.62f,
            amplitude = animatedAmplitude * 1.05f,
            wavelength = width * 0.88f,
            phase = timePhase * -1.15f + 1.5f,
            color = animatedColor2.copy(alpha = if (isDark) 0.15f else 0.09f),
            fillToBottom = false
        )

        drawWaveRibbon(
            width = width,
            height = height,
            baseY = height * 0.76f,
            amplitude = animatedAmplitude * 1.25f,
            wavelength = width * 0.95f,
            phase = timePhase * 0.9f + 2.8f,
            color = animatedColor3.copy(alpha = if (isDark) 0.16f else 0.10f),
            fillToBottom = true
        )

        // 3. User interaction ripple waves
        val currentTime = System.currentTimeMillis()
        val activeRipples = ripples.filter { currentTime - it.birthTime < 1800 }
        if (activeRipples.size != ripples.size) {
            ripples.clear()
            ripples.addAll(activeRipples)
        }

        ripples.forEach { ripple ->
            val elapsed = (currentTime - ripple.birthTime).toFloat() / 1800f
            val rippleRadius = elapsed * width * 0.75f
            val rippleAlpha = (1f - elapsed).coerceIn(0f, 1f) * 0.25f

            drawCircle(
                color = animatedColor1.copy(alpha = rippleAlpha),
                radius = rippleRadius,
                center = ripple.center,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx() * (1f - elapsed))
            )
        }
    }
}

private fun DrawScope.drawWaveRibbon(
    width: Float,
    height: Float,
    baseY: Float,
    amplitude: Float,
    wavelength: Float,
    phase: Float,
    color: Color,
    fillToBottom: Boolean
) {
    val path = Path()
    val steps = 36
    val stepSize = width / steps

    val startY = baseY + (sin(phase) * amplitude)
    path.moveTo(0f, startY)

    for (i in 1..steps) {
        val x = i * stepSize
        val normalizedX = (x / wavelength) * (2 * Math.PI).toFloat()
        val y = baseY + (sin(normalizedX + phase) * amplitude)
        val prevX = (i - 1) * stepSize
        val prevNormalizedX = (prevX / wavelength) * (2 * Math.PI).toFloat()
        val prevY = baseY + (sin(prevNormalizedX + phase) * amplitude)

        val cx = (prevX + x) / 2f
        path.quadraticTo(prevX, prevY, cx, (prevY + y) / 2f)
    }

    if (fillToBottom) {
        path.lineTo(width, height)
        path.lineTo(0f, height)
        path.close()
        drawPath(
            path = path,
            brush = Brush.verticalGradient(
                colors = listOf(color, color.copy(alpha = 0.01f)),
                startY = baseY - amplitude,
                endY = height
            )
        )
    } else {
        path.lineTo(width, baseY + amplitude * 2.2f)
        path.lineTo(0f, baseY + amplitude * 2.2f)
        path.close()
        drawPath(
            path = path,
            brush = Brush.verticalGradient(
                colors = listOf(color, Color.Transparent),
                startY = baseY - amplitude,
                endY = baseY + amplitude * 2.2f
            )
        )
    }
}
