package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MotionPhotosOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MoodType
import com.example.ui.theme.DesignPreset
import com.example.ui.theme.LocalVibeExtraColors

@Composable
fun VibeTopBar(
    preset: DesignPreset,
    isDark: Boolean,
    currentMood: MoodType,
    reducedMotion: Boolean,
    isCloudSynced: Boolean,
    onToggleDark: () -> Unit,
    onOpenSync: () -> Unit
) {
    val extraColors = LocalVibeExtraColors.current
    val rotation by animateFloatAsState(
        targetValue = if (isDark) 360f else 0f,
        animationSpec = spring(stiffness = 300f),
        label = "theme_icon_rot"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .testTag("vibe_top_bar"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // App identity with subtle mood pulse indicator
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, extraColors.border, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✦",
                    color = currentMood.primaryColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Forge by uSafe",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(currentMood.primaryColor.copy(alpha = 0.18f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = preset.displayName,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = currentMood.primaryColor
                            )
                        )
                    }
                }
                Text(
                    text = "DevOps & Vibe Studio",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        color = extraColors.textMuted
                    )
                )
            }
        }

        // Actions: Reduced motion badge, Cloud Sync pill, Theme toggle
        Row(verticalAlignment = Alignment.CenterVertically) {
            AnimatedVisibility(visible = reducedMotion) {
                Box(
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MotionPhotosOff,
                        contentDescription = "Reduced Motion Active",
                        modifier = Modifier.size(16.dp),
                        tint = extraColors.textMuted
                    )
                }
            }

            // Sync pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, extraColors.border, RoundedCornerShape(20.dp))
                    .clickable { onOpenSync() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .testTag("sync_status_pill"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isCloudSynced) Icons.Default.CloudDone else Icons.Default.Cloud,
                        contentDescription = "Cloud Status",
                        modifier = Modifier.size(14.dp),
                        tint = if (isCloudSynced) Color(0xFF10B981) else extraColors.textMuted
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isCloudSynced) "uSafe Synced" else "uSafe Cloud",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Dark/Light Mode toggle with fluid spring physics
            IconButton(
                onClick = onToggleDark,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, extraColors.border, CircleShape)
                    .testTag("theme_toggle_button")
            ) {
                Icon(
                    imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = if (isDark) "Switch to Light Mode" else "Switch to Dark Mode",
                    modifier = Modifier
                        .size(18.dp)
                        .rotate(rotation),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
