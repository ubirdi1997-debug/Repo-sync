package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CloudDone
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.FluidBackgroundCanvas
import com.example.ui.components.VibeTopBar
import com.example.ui.screens.ProjectToolsScreen
import com.example.ui.screens.ProjectsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.StudioScreen
import com.example.ui.theme.LocalVibeExtraColors
import com.example.ui.theme.VibeTheme
import com.example.viewmodel.NavigationTab
import com.example.viewmodel.VibeViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: VibeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val preset by viewModel.selectedPreset.collectAsStateWithLifecycle()
            val isDark by viewModel.isDarkMode.collectAsStateWithLifecycle()
            val highContrast by viewModel.highContrast.collectAsStateWithLifecycle()
            val reducedMotion by viewModel.reducedMotion.collectAsStateWithLifecycle()
            val waveSpeed by viewModel.waveSpeed.collectAsStateWithLifecycle()
            val currentMood by viewModel.selectedMood.collectAsStateWithLifecycle()
            val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
            val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()
            val authToken by viewModel.authToken.collectAsStateWithLifecycle()
            val behaviorPrompt by viewModel.behaviorPrompt.collectAsStateWithLifecycle()
            val isGenerating by viewModel.isGenerating.collectAsStateWithLifecycle()

            val snackbarHostState = remember { SnackbarHostState() }

            LaunchedEffect(userMessage) {
                userMessage?.let { msg ->
                    snackbarHostState.showSnackbar(msg)
                    viewModel.clearUserMessage()
                }
            }

            VibeTheme(
                preset = preset,
                darkTheme = isDark,
                highContrast = highContrast
            ) {
                val extraColors = LocalVibeExtraColors.current

                Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                    // Fluid Abstract Background Animation Canvas reacting to mood and prompt
                    FluidBackgroundCanvas(
                        mood = currentMood,
                        waveColors = extraColors.waveColors,
                        isDark = isDark,
                        reducedMotion = reducedMotion,
                        speedMultiplier = waveSpeed,
                        userPrompt = behaviorPrompt,
                        isGenerating = isGenerating
                    )

                    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                        val isExpandedScreen = maxWidth >= 600.dp

                        if (isExpandedScreen) {
                            // Adaptive Layout for Tablets / Foldables: Navigation Rail on the side
                            Row(modifier = Modifier.fillMaxSize()) {
                                NavigationRail(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .border(width = 1.dp, color = extraColors.border),
                                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                                ) {
                                    NavigationTab.values().forEach { tab ->
                                        val isSelected = tab == currentTab
                                        NavigationRailItem(
                                            selected = isSelected,
                                            onClick = { viewModel.selectTab(tab) },
                                            icon = {
                                                TabIcon(tab = tab, isSelected = isSelected)
                                            },
                                            label = { Text(tab.title, fontSize = 11.sp) },
                                            colors = NavigationRailItemDefaults.colors(
                                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                                unselectedIconColor = extraColors.textMuted,
                                                unselectedTextColor = extraColors.textMuted
                                            ),
                                            modifier = Modifier.testTag("nav_rail_${tab.name.lowercase()}")
                                        )
                                    }
                                }

                                Scaffold(
                                    modifier = Modifier.weight(1f),
                                    containerColor = androidx.compose.ui.graphics.Color.Transparent,
                                    topBar = {
                                        VibeTopBar(
                                            preset = preset,
                                            isDark = isDark,
                                            currentMood = currentMood,
                                            reducedMotion = reducedMotion,
                                            isCloudSynced = authToken != null,
                                            onToggleDark = { viewModel.toggleDarkMode() },
                                            onOpenSync = { viewModel.selectTab(NavigationTab.SETTINGS) }
                                        )
                                    },
                                    snackbarHost = { SnackbarHost(snackbarHostState) },
                                    contentWindowInsets = WindowInsets.safeDrawing
                                ) { paddingValues ->
                                    ScreenContent(
                                        currentTab = currentTab,
                                        viewModel = viewModel,
                                        modifier = Modifier.padding(paddingValues)
                                    )
                                }
                            }
                        } else {
                            // Handheld / Compact Layout: Bottom Navigation Bar
                            Scaffold(
                                modifier = Modifier.fillMaxSize(),
                                containerColor = androidx.compose.ui.graphics.Color.Transparent,
                                topBar = {
                                    VibeTopBar(
                                        preset = preset,
                                        isDark = isDark,
                                        currentMood = currentMood,
                                        reducedMotion = reducedMotion,
                                        isCloudSynced = authToken != null,
                                        onToggleDark = { viewModel.toggleDarkMode() },
                                        onOpenSync = { viewModel.selectTab(NavigationTab.SETTINGS) }
                                    )
                                },
                                bottomBar = {
                                    NavigationBar(
                                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                                        modifier = Modifier
                                            .border(width = 1.dp, color = extraColors.border)
                                            .testTag("bottom_navigation_bar"),
                                        windowInsets = WindowInsets.navigationBars
                                    ) {
                                        NavigationTab.values().forEach { tab ->
                                            val isSelected = tab == currentTab
                                            NavigationBarItem(
                                                selected = isSelected,
                                                onClick = { viewModel.selectTab(tab) },
                                                icon = {
                                                    TabIcon(tab = tab, isSelected = isSelected)
                                                },
                                                label = {
                                                    Text(
                                                        text = tab.title,
                                                        fontSize = 11.sp,
                                                        fontWeight = if (isSelected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal
                                                    )
                                                },
                                                colors = NavigationBarItemDefaults.colors(
                                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                                    unselectedIconColor = extraColors.textMuted,
                                                    unselectedTextColor = extraColors.textMuted
                                                ),
                                                modifier = Modifier.testTag("nav_item_${tab.name.lowercase()}")
                                            )
                                        }
                                    }
                                },
                                snackbarHost = { SnackbarHost(snackbarHostState) },
                                contentWindowInsets = WindowInsets.safeDrawing
                            ) { paddingValues ->
                                ScreenContent(
                                    currentTab = currentTab,
                                    viewModel = viewModel,
                                    modifier = Modifier.padding(paddingValues)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TabIcon(tab: NavigationTab, isSelected: Boolean) {
    val icon = when (tab) {
        NavigationTab.STUDIO -> if (isSelected) Icons.Filled.AutoAwesome else Icons.Outlined.AutoAwesome
        NavigationTab.SPACES -> if (isSelected) Icons.Filled.Folder else Icons.Outlined.Folder
        NavigationTab.TOOLS -> if (isSelected) Icons.Filled.Terminal else Icons.Outlined.Terminal
        NavigationTab.SETTINGS -> if (isSelected) Icons.Filled.Tune else Icons.Outlined.Tune
    }

    if (tab.badge != null) {
        BadgedBox(
            badge = {
                Badge(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Text(tab.badge, fontSize = 9.sp)
                }
            }
        ) {
            Icon(imageVector = icon, contentDescription = tab.title, modifier = Modifier.size(22.dp))
        }
    } else {
        Icon(imageVector = icon, contentDescription = tab.title, modifier = Modifier.size(22.dp))
    }
}

@Composable
private fun ScreenContent(
    currentTab: NavigationTab,
    viewModel: VibeViewModel,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = currentTab,
        transitionSpec = {
            fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(180))
        },
        label = "screen_navigation_content",
        modifier = modifier
    ) { targetTab ->
        when (targetTab) {
            NavigationTab.STUDIO -> StudioScreen(viewModel = viewModel)
            NavigationTab.SPACES -> ProjectsScreen(viewModel = viewModel)
            NavigationTab.TOOLS -> ProjectToolsScreen(viewModel = viewModel)
            NavigationTab.SETTINGS -> SettingsScreen(viewModel = viewModel)
        }
    }
}
