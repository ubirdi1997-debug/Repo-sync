package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MotionPhotosOff
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.DesignPreset
import com.example.ui.theme.LocalVibeExtraColors
import com.example.viewmodel.VibeViewModel

@Composable
fun DesignLabScreen(
    viewModel: VibeViewModel,
    modifier: Modifier = Modifier
) {
    val selectedPreset by viewModel.selectedPreset.collectAsStateWithLifecycle()
    val isDark by viewModel.isDarkMode.collectAsStateWithLifecycle()
    val reducedMotion by viewModel.reducedMotion.collectAsStateWithLifecycle()
    val highContrast by viewModel.highContrast.collectAsStateWithLifecycle()
    val waveSpeed by viewModel.waveSpeed.collectAsStateWithLifecycle()
    val extraColors = LocalVibeExtraColors.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("design_lab_screen"),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Step 1 Header
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Phase 1 • Design Directory",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Curated Minimalist Aesthetics",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Select your preferred visual foundation. Fluid animations, contrast ratios, and color tokens update immediately across both Light and Dark modes.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = extraColors.textMuted
                )
            }
        }

        // 3 Curated Design Choices
        items(DesignPreset.values().size) { index ->
            val preset = DesignPreset.values()[index]
            val isSelected = preset == selectedPreset

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else extraColors.border,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { viewModel.selectDesignPreset(preset) }
                    .testTag("preset_card_${preset.id}"),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                                contentDescription = if (isSelected) "Selected" else "Unselected",
                                tint = if (isSelected) MaterialTheme.colorScheme.primary else extraColors.textMuted,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = preset.displayName,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "ACTIVE",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = preset.tagline,
                        style = MaterialTheme.typography.bodySmall,
                        color = extraColors.textMuted
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Color swatches preview
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ColorSwatchPreview("Background", if (isDark) preset.darkBackground else preset.lightBackground)
                        ColorSwatchPreview("Surface", if (isDark) preset.darkSurface else preset.lightSurface)
                        ColorSwatchPreview("Primary", if (isDark) preset.darkPrimary else preset.lightPrimary)
                        ColorSwatchPreview("Secondary", if (isDark) preset.darkSecondary else preset.lightSecondary)
                        ColorSwatchPreview("Border", if (isDark) preset.darkBorder else preset.lightBorder)
                    }
                }
            }
        }

        // Fluid Abstract Background Controls
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, extraColors.border, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Waves,
                            contentDescription = "Wave motion",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Fluid Background Motion Engine",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Physics-based sinusoidal waves & particle luminescence simulating Framer Motion dynamics natively at 60/120fps.",
                        style = MaterialTheme.typography.bodySmall,
                        color = extraColors.textMuted
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Wave Velocity (${String.format("%.1f", waveSpeed)}x)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Slider(
                        value = waveSpeed,
                        onValueChange = { viewModel.setWaveSpeed(it) },
                        valueRange = 0.3f..2.5f,
                        steps = 8,
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.testTag("wave_speed_slider")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Accessibility Reduced Motion
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Reduced Motion (Accessibility)",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Halts high-frequency waves for motion sensitivity compliance.",
                                style = MaterialTheme.typography.bodySmall,
                                color = extraColors.textMuted
                            )
                        }
                        Switch(
                            checked = reducedMotion,
                            onCheckedChange = { viewModel.toggleReducedMotion() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.testTag("reduced_motion_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // High Contrast Borders
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "High Contrast Micro-Borders",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Enhances edge sharpness on low-luminance displays.",
                                style = MaterialTheme.typography.bodySmall,
                                color = extraColors.textMuted
                            )
                        }
                        Switch(
                            checked = highContrast,
                            onCheckedChange = { viewModel.toggleHighContrast() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.testTag("high_contrast_switch")
                        )
                    }
                }
            }
        }

        // Live Light / Dark Mode Fluid Transition Demo
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, extraColors.border, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Light & Dark Mode Transitions",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Interpolated with physics spring damping (85% ratio) for seamless ocular adaptation.",
                        style = MaterialTheme.typography.bodySmall,
                        color = extraColors.textMuted
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.toggleDarkMode() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("toggle_theme_demo_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isDark) "Switch to Light Mode" else "Switch to Dark Mode",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // 5-Part Plan Status Roadmap
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, extraColors.border, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Application Roadmap Alignment",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    PlanStepItem(1, "Select Design from Directory", "3 distinct aesthetics + fluid canvas waves active", true)
                    PlanStepItem(2, "Menu Structure & Navigation Flow", "Adaptive Bottom Bar & Rail, 4 primary hubs", true)
                    PlanStepItem(3, "Data Models & Screen Interactions", "Moods, LogicBlueprint, state machine, sandbox", true)
                    PlanStepItem(4, "Room Database Connection", "Local SQLite persistence via VibeProjectDao & Flow", true)
                    PlanStepItem(5, "uSafe Authentication & Cloud Sync", "auth.usafe.in login, api.usafe.in project mirroring", true)
                }
            }
        }
    }
}

@Composable
private fun ColorSwatchPreview(label: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color)
                .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun PlanStepItem(stepNumber: Int, title: String, subtitle: String, isDone: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Completed",
            tint = if (isDone) Color(0xFF10B981) else Color(0xFF94A3B8),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = "Part $stepNumber: $title",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                color = LocalVibeExtraColors.current.textMuted
            )
        }
    }
}
