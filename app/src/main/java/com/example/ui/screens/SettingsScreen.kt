package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.InferenceProvider
import com.example.data.remote.NetworkResult
import com.example.ui.theme.DesignPreset
import com.example.ui.theme.LocalVibeExtraColors
import com.example.viewmodel.VibeViewModel

@Composable
fun SettingsScreen(
    viewModel: VibeViewModel,
    modifier: Modifier = Modifier
) {
    val selectedPreset by viewModel.selectedPreset.collectAsStateWithLifecycle()
    val isDark by viewModel.isDarkMode.collectAsStateWithLifecycle()
    val reducedMotion by viewModel.reducedMotion.collectAsStateWithLifecycle()
    val highContrast by viewModel.highContrast.collectAsStateWithLifecycle()
    val waveSpeed by viewModel.waveSpeed.collectAsStateWithLifecycle()

    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val authToken by viewModel.authToken.collectAsStateWithLifecycle()
    val authResult by viewModel.authResult.collectAsStateWithLifecycle()
    val syncResult by viewModel.syncResult.collectAsStateWithLifecycle()
    val endpointHealth by viewModel.endpointHealth.collectAsStateWithLifecycle()
    val projects by viewModel.projects.collectAsStateWithLifecycle()

    val selectedProvider by viewModel.selectedProvider.collectAsStateWithLifecycle()
    val providerConfigs by viewModel.providerConfigs.collectAsStateWithLifecycle()
    val extraColors = LocalVibeExtraColors.current

    // Accordions expanded states
    var isUsafeSyncOpen by remember { mutableStateOf(true) }
    var isDesignThemesOpen by remember { mutableStateOf(false) }
    var isWaveMotionOpen by remember { mutableStateOf(false) }
    var isByokInferenceOpen by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("settings_screen"),
        contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = "Settings & Forge Control",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Unified workspace configuration: uSafe Cloud, BYOK Inference models, visual themes, and motion physics.",
                    style = MaterialTheme.typography.bodySmall,
                    color = extraColors.textMuted
                )
            }
        }

        // Accordion 1: BYOK Inference Models (Bedrock, DigitalOcean, Vertex, Gemini, OpenClaw)
        item {
            AccordionCard(
                title = "BYOK Inference Models",
                subtitle = "Active: ${selectedProvider.displayName} (${selectedProvider.defaultModel})",
                icon = Icons.Default.Key,
                isOpen = isByokInferenceOpen,
                onToggle = { isByokInferenceOpen = !isByokInferenceOpen }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Bring Your Own Key (BYOK)",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Connect AWS Bedrock, DigitalOcean GenAI, Google Cloud Vertex, Gemini AI, or uSafe OpenClaw proxy. Keys are encrypted locally.",
                        style = MaterialTheme.typography.bodySmall,
                        color = extraColors.textMuted
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Provider Selector Chips
                    InferenceProvider.values().forEach { provider ->
                        val isSelected = provider == selectedProvider
                        val currentConfig = providerConfigs[provider.id]
                        var keyInput by remember(provider.id, currentConfig?.apiKey) {
                            mutableStateOf(currentConfig?.apiKey ?: "")
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else extraColors.border,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { viewModel.selectInferenceProvider(provider) },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                                            contentDescription = null,
                                            tint = if (isSelected) MaterialTheme.colorScheme.primary else extraColors.textMuted,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = provider.displayName,
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = "${provider.company} • ${provider.defaultModel}",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = extraColors.textMuted
                                                )
                                            )
                                        }
                                    }

                                    if (provider.isDefaultConfigured) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(Color(0xFF10B981).copy(alpha = 0.2f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "uSafe Ready",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontSize = 9.sp,
                                                    color = Color(0xFF10B981),
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                    }
                                }

                                if (isSelected) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = provider.description,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                        color = extraColors.textMuted
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    OutlinedTextField(
                                        value = keyInput,
                                        onValueChange = { keyInput = it },
                                        label = { Text("API Key (${provider.displayName})") },
                                        placeholder = { Text("sk-...", fontSize = 11.sp) },
                                        modifier = Modifier.fillMaxWidth(),
                                        singleLine = true,
                                        visualTransformation = PasswordVisualTransformation(),
                                        shape = RoundedCornerShape(8.dp)
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Button(
                                        onClick = {
                                            viewModel.updateProviderKey(
                                                providerId = provider.id,
                                                key = keyInput,
                                                model = provider.defaultModel
                                            )
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = MaterialTheme.colorScheme.onPrimary
                                        )
                                    ) {
                                        Text("Save & Activate BYOK Key", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Accordion 2: uSafe Cloud & Multi-Device Sync
        item {
            AccordionCard(
                title = "uSafe Cloud & Device Sync",
                subtitle = if (currentUser != null) "${currentUser?.email} (Connected)" else "auth.usafe.in (Offline)",
                icon = Icons.Default.CloudSync,
                isOpen = isUsafeSyncOpen,
                onToggle = { isUsafeSyncOpen = !isUsafeSyncOpen }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Endpoint Status Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "api.usafe.in: ${endpointHealth.latencyMs}ms latency",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFF10B981)
                                )
                            )
                        }

                        IconButton(
                            onClick = { viewModel.checkEndpointHealth() },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Ping", modifier = Modifier.size(14.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (currentUser != null) {
                        // User Account Summary
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = currentUser?.email ?: "",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${projects.size} Projects • ${currentUser?.tier ?: "uSafe Pro"}",
                                        style = MaterialTheme.typography.labelSmall.copy(color = extraColors.textMuted)
                                    )
                                }

                                OutlinedButton(
                                    onClick = { viewModel.logout() },
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("Sign Out", fontSize = 11.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { viewModel.syncAllProjectsToCloud() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Icon(Icons.Default.CloudSync, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Sync All Spaces to Cloud (${projects.size})", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        // Sign-in Mini Form
                        var emailInput by remember { mutableStateOf("developer@usafe.in") }
                        var passInput by remember { mutableStateOf("••••••••") }

                        OutlinedTextField(
                            value = emailInput,
                            onValueChange = { emailInput = it },
                            label = { Text("uSafe Identity Email") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = passInput,
                            onValueChange = { passInput = it },
                            label = { Text("Password") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation()
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { viewModel.loginWithUSafe(emailInput, passInput) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Authorize & Sync via auth.usafe.in")
                        }
                    }
                }
            }
        }

        // Accordion 3: Design & Visual Themes
        item {
            AccordionCard(
                title = "Design & Visual Themes",
                subtitle = "${selectedPreset.displayName} • ${if (isDark) "Dark Mode" else "Light Mode"}",
                icon = Icons.Default.Palette,
                isOpen = isDesignThemesOpen,
                onToggle = { isDesignThemesOpen = !isDesignThemesOpen }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Curated Minimalist Presets",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    DesignPreset.values().forEach { preset ->
                        val isSelected = preset == selectedPreset
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else extraColors.border,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { viewModel.selectDesignPreset(preset) }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (isSelected) MaterialTheme.colorScheme.primary else extraColors.textMuted,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = preset.displayName,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = preset.tagline,
                                    style = MaterialTheme.typography.labelSmall.copy(color = extraColors.textMuted)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Theme Mode Toggle
                    Button(
                        onClick = { viewModel.toggleDarkMode() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Icon(
                            imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (isDark) "Switch to Light Mode" else "Switch to Dark Mode")
                    }
                }
            }
        }

        // Accordion 4: Fluid Wave & Motion Controls
        item {
            AccordionCard(
                title = "Fluid Motion & Accessibility",
                subtitle = "Speed: ${String.format("%.1f", waveSpeed)}x • Motion: ${if (reducedMotion) "Reduced" else "Full"}",
                icon = Icons.Default.Waves,
                isOpen = isWaveMotionOpen,
                onToggle = { isWaveMotionOpen = !isWaveMotionOpen }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Wave Dynamics (${String.format("%.1f", waveSpeed)}x)",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Slider(
                        value = waveSpeed,
                        onValueChange = { viewModel.setWaveSpeed(it) },
                        valueRange = 0.3f..2.5f,
                        steps = 8,
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Reduced motion switch
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
                                text = "Stops wave oscillation for sensitive vision",
                                style = MaterialTheme.typography.bodySmall,
                                color = extraColors.textMuted
                            )
                        }
                        Switch(
                            checked = reducedMotion,
                            onCheckedChange = { viewModel.toggleReducedMotion() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // High contrast switch
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
                                text = "Sharpens boundaries in low-light environments",
                                style = MaterialTheme.typography.bodySmall,
                                color = extraColors.textMuted
                            )
                        }
                        Switch(
                            checked = highContrast,
                            onCheckedChange = { viewModel.toggleHighContrast() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AccordionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isOpen: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    val extraColors = LocalVibeExtraColors.current
    val arrowRotation by animateFloatAsState(
        targetValue = if (isOpen) 180f else 0f,
        label = "accordion_arrow"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, extraColors.border, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
        )
    ) {
        Column {
            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelSmall.copy(color = extraColors.textMuted)
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = if (isOpen) "Collapse" else "Expand",
                    tint = extraColors.textMuted,
                    modifier = Modifier.rotate(arrowRotation)
                )
            }

            AnimatedVisibility(visible = isOpen) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = extraColors.border.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(0.dp)
                        )
                ) {
                    content()
                }
            }
        }
    }
}
