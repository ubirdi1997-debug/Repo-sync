package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.AutoMode
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VpnKey
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AgentPermissionMode
import com.example.data.model.SshAuthType
import com.example.data.model.SshStatus
import com.example.data.model.ToolInvocation
import com.example.data.model.WorkspaceNode
import com.example.data.model.WorkspaceViewMode
import com.example.ui.theme.LocalVibeExtraColors
import com.example.viewmodel.VibeViewModel

@Composable
fun ProjectToolsScreen(
    viewModel: VibeViewModel,
    modifier: Modifier = Modifier
) {
    val gitConfig by viewModel.gitConfig.collectAsStateWithLifecycle()
    val isGitPushing by viewModel.isGitPushing.collectAsStateWithLifecycle()
    val sshServers by viewModel.sshServers.collectAsStateWithLifecycle()
    val agentPermission by viewModel.agentPermission.collectAsStateWithLifecycle()
    val workspaceViewMode by viewModel.workspaceViewMode.collectAsStateWithLifecycle()
    val workspaceFiles by viewModel.workspaceFiles.collectAsStateWithLifecycle()
    val selectedFile by viewModel.selectedWorkspaceFile.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val generatedLogic by viewModel.generatedLogic.collectAsStateWithLifecycle()
    val extraColors = LocalVibeExtraColors.current

    var commitMsgInput by remember { mutableStateOf("feat: implement reactive vibe state machine") }
    var chatPromptInput by remember { mutableStateOf("") }
    var showAddServerDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("project_tools_screen"),
        contentPadding = PaddingValues(top = 8.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
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
                            text = "Project Tools & DevOps Engine",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "DevOps & Cloud Automation",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Auto-commit to GitHub, manage OpenSSH server deployments, toggle between code and workspace directory, and guide Forge AI in Auto-Pilot mode.",
                    style = MaterialTheme.typography.bodySmall,
                    color = extraColors.textMuted
                )
            }
        }

        // Card 1: Workspace Mode Toggle (Preview vs Code vs File Directory)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, extraColors.border, RoundedCornerShape(16.dp))
                    .testTag("workspace_viewer_card"),
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
                        Text(
                            text = "Workspace Viewer",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        // 3-Way Mode Toggle Tabs
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(2.dp),
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            WorkspaceViewMode.values().forEach { mode ->
                                val isSelected = mode == workspaceViewMode
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                                        )
                                        .clickable { viewModel.setWorkspaceViewMode(mode) }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                        .testTag("workspace_tab_${mode.name.lowercase()}")
                                ) {
                                    Text(
                                        text = mode.title,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else extraColors.textMuted
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    when (workspaceViewMode) {
                        WorkspaceViewMode.PREVIEW -> {
                            // UI Live Preview Box
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF070B14))
                                    .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                                            .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "✦", fontSize = 24.sp, color = MaterialTheme.colorScheme.primary)
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = generatedLogic?.title ?: "Vibe Live Simulation",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Active State: ${generatedLogic?.stateMachineStates?.firstOrNull() ?: "HARMONIZING"}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "60 FPS Reactive Rendering",
                                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF10B981))
                                    )
                                }
                            }
                        }

                        WorkspaceViewMode.CODE -> {
                            // Source Code View
                            SelectionContainer {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF090D16))
                                        .border(1.dp, extraColors.border, RoundedCornerShape(12.dp))
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = generatedLogic?.generatedCode ?: "// No code generated yet. Open Forge Studio.",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 11.sp,
                                            color = Color(0xFFE2E8F0),
                                            lineHeight = 16.sp
                                        )
                                    )
                                }
                            }
                        }

                        WorkspaceViewMode.DIRECTORY -> {
                            // Files Tree Explorer
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF090D16))
                                    .border(1.dp, extraColors.border, RoundedCornerShape(12.dp))
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "Workspace Root (/workspace)",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = extraColors.textMuted,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                                WorkspaceTreeView(
                                    nodes = workspaceFiles,
                                    selectedNode = selectedFile,
                                    onSelect = { viewModel.selectWorkspaceFile(it) }
                                )
                            }
                        }
                    }

                    selectedFile?.previewContent?.let { content ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Inspecting: ${selectedFile?.name}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }
        }

        // Card 2: GitHub Auto-Commit & Cloud Sync
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, extraColors.border, RoundedCornerShape(16.dp))
                    .testTag("github_tools_card"),
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
                                imageVector = Icons.Default.AltRoute,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "GitHub Auto-Commit & Sync",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        // Auto-commit toggle
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Auto",
                                style = MaterialTheme.typography.labelSmall.copy(color = extraColors.textMuted)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Switch(
                                checked = gitConfig.autoCommit,
                                onCheckedChange = { viewModel.toggleAutoCommit(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier.testTag("auto_commit_switch")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Repository: ${gitConfig.repoUrl} (Branch: ${gitConfig.branch})",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = FontFamily.Monospace,
                            color = extraColors.textMuted
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = commitMsgInput,
                        onValueChange = { commitMsgInput = it },
                        label = { Text("Commit Message") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("git_commit_message_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = extraColors.border
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.commitAndPushGit(commitMsgInput) },
                        enabled = !isGitPushing && commitMsgInput.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("commit_push_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        if (isGitPushing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Committing & Pushing to GitHub...")
                        } else {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Commit & Push to ${gitConfig.branch}", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Last Push Status
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF10B981))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = gitConfig.lastPushStatus,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF10B981),
                                fontFamily = FontFamily.Monospace
                            )
                        )
                    }
                }
            }
        }

        // Card 3: SSH Server Connection (OpenSSH Keys & Direct Credentials)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, extraColors.border, RoundedCornerShape(16.dp))
                    .testTag("ssh_tools_card"),
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
                                imageVector = Icons.Default.Terminal,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "SSH Server Connections",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        Button(
                            onClick = { showAddServerDialog = !showAddServerDialog },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Host", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Connect directly with saved ID/password or OpenSSH private keys for automated server deployment.",
                        style = MaterialTheme.typography.bodySmall,
                        color = extraColors.textMuted
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    sshServers.forEach { server ->
                        val isConnected = server.status == SshStatus.CONNECTED
                        val isConnecting = server.status == SshStatus.CONNECTING

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .border(1.dp, extraColors.border, RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(Color(server.status.colorHex))
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = server.name,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    Text(
                                        text = "${server.username}@${server.host}:${server.port} • ${server.authType.title}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontFamily = FontFamily.Monospace,
                                            color = extraColors.textMuted
                                        )
                                    )
                                    if (server.lastPingMs != null) {
                                        Text(
                                            text = "Active latency: ${server.lastPingMs}ms",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = Color(0xFF10B981)
                                            )
                                        )
                                    }
                                }

                                Button(
                                    onClick = {
                                        if (isConnected) viewModel.disconnectSshServer(server.id)
                                        else viewModel.connectSshServer(server.id)
                                    },
                                    enabled = !isConnecting,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isConnected) Color(0xFFEF4444).copy(alpha = 0.2f) else MaterialTheme.colorScheme.primary,
                                        contentColor = if (isConnected) Color(0xFFEF4444) else MaterialTheme.colorScheme.onPrimary
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.testTag("ssh_action_${server.id}")
                                ) {
                                    if (isConnecting) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(14.dp),
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            strokeWidth = 2.dp
                                        )
                                    } else {
                                        Text(
                                            text = if (isConnected) "Disconnect" else "Connect",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Quick Add Server Expandable Form
                    AnimatedVisibility(visible = showAddServerDialog) {
                        var newHost by remember { mutableStateOf("") }
                        var newUsername by remember { mutableStateOf("root") }
                        var newCredential by remember { mutableStateOf("") }
                        var newAuthType by remember { mutableStateOf(SshAuthType.OPENSSH_KEY) }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF090D16))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "Register New SSH Server",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = newHost,
                                onValueChange = { newHost = it },
                                label = { Text("Host / IP (e.g. server.usafe.in)") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedTextField(
                                value = newUsername,
                                onValueChange = { newUsername = it },
                                label = { Text("SSH Username") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedTextField(
                                value = newCredential,
                                onValueChange = { newCredential = it },
                                label = { Text(if (newAuthType == SshAuthType.OPENSSH_KEY) "Paste OpenSSH Private Key" else "Password") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = if (newAuthType == SshAuthType.OPENSSH_KEY) 2 else 1
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = {
                                    if (newHost.isNotBlank()) {
                                        viewModel.addSshServer(
                                            name = "Server ($newHost)",
                                            host = newHost,
                                            port = 22,
                                            username = newUsername,
                                            authType = newAuthType,
                                            credential = newCredential
                                        )
                                        showAddServerDialog = false
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Save SSH Server")
                            }
                        }
                    }
                }
            }
        }

        // Card 4: Forge AI Agent & Permissions (Auto-Pilot vs Interactive)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, extraColors.border, RoundedCornerShape(16.dp))
                    .testTag("ai_agent_tools_card"),
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
                            Text(text = agentPermission.iconEmoji, fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Forge AI Agent Assistant",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = "Permission Mode: ${agentPermission.title}",
                                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.primary)
                                )
                            }
                        }

                        // Auto-Pilot vs Interactive Chips
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            FilterChip(
                                selected = agentPermission == AgentPermissionMode.AUTO_PILOT,
                                onClick = { viewModel.setAgentPermission(AgentPermissionMode.AUTO_PILOT) },
                                label = { Text("Auto-Pilot", fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                    selectedLabelColor = MaterialTheme.colorScheme.primary
                                )
                            )

                            FilterChip(
                                selected = agentPermission == AgentPermissionMode.INTERACTIVE,
                                onClick = { viewModel.setAgentPermission(AgentPermissionMode.INTERACTIVE) },
                                label = { Text("Interactive", fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFFF59E0B).copy(alpha = 0.2f),
                                    selectedLabelColor = Color(0xFFF59E0B)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = agentPermission.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = extraColors.textMuted
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Chat messages list
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF090D16))
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        chatMessages.takeLast(4).forEach { msg ->
                            val isUser = msg.sender == "user"
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isUser) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else Color(0xFF141926))
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = if (isUser) "You" else "Forge AI",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isUser) MaterialTheme.colorScheme.primary else Color(0xFF38BDF8)
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = msg.content,
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFE2E8F0))
                                )

                                // Tool Invocation Card inside Chat Bubble
                                msg.toolInvocation?.let { tool ->
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color(0xFF070A0F))
                                            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                            .padding(8.dp)
                                    ) {
                                        Column {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Tool: ${tool.toolName}",
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                )
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(4.dp))
                                                        .background(
                                                            if (tool.status == "EXECUTED" || tool.status == "CONFIRMED") Color(0xFF10B981).copy(alpha = 0.2f)
                                                            else Color(0xFFF59E0B).copy(alpha = 0.2f)
                                                        )
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = tool.status,
                                                        style = MaterialTheme.typography.labelSmall.copy(
                                                            fontSize = 9.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = if (tool.status == "EXECUTED" || tool.status == "CONFIRMED") Color(0xFF10B981) else Color(0xFFF59E0B)
                                                        )
                                                    )
                                                }
                                            }
                                            Text(
                                                text = tool.command,
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontFamily = FontFamily.Monospace,
                                                    fontSize = 10.sp,
                                                    color = Color(0xFF94A3B8)
                                                )
                                            )

                                            // If interactive and pending, show confirmation actions
                                            if (tool.status == "PENDING") {
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                    Button(
                                                        onClick = { viewModel.confirmToolExecution(tool) },
                                                        colors = ButtonDefaults.buttonColors(
                                                            containerColor = Color(0xFF10B981)
                                                        ),
                                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                                        shape = RoundedCornerShape(6.dp)
                                                    ) {
                                                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(12.dp))
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text("Approve", fontSize = 10.sp)
                                                    }

                                                    OutlinedButton(
                                                        onClick = { viewModel.denyToolExecution(tool) },
                                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                                        shape = RoundedCornerShape(6.dp)
                                                    ) {
                                                        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(12.dp))
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text("Deny", fontSize = 10.sp)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Input bar to direct the chatbot
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = chatPromptInput,
                            onValueChange = { chatPromptInput = it },
                            placeholder = { Text("e.g. 'connect to ssh', 'commit to github', 'run cli'", fontSize = 12.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("agent_chat_input"),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                viewModel.sendAiChatPrompt(chatPromptInput)
                                chatPromptInput = ""
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.primary)
                                .testTag("send_agent_prompt_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WorkspaceTreeView(
    nodes: List<WorkspaceNode>,
    selectedNode: WorkspaceNode?,
    onSelect: (WorkspaceNode) -> Unit,
    depth: Int = 0
) {
    Column {
        nodes.forEach { node ->
            var isExpanded by remember { mutableStateOf(depth == 0) }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = (depth * 14).dp, top = 2.dp, bottom = 2.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (node == selectedNode) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
                    .clickable {
                        if (node.isDirectory) {
                            isExpanded = !isExpanded
                        } else {
                            onSelect(node)
                        }
                    }
                    .padding(horizontal = 6.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (node.isDirectory) {
                        if (isExpanded) Icons.Default.FolderOpen else Icons.Default.Folder
                    } else {
                        Icons.Default.InsertDriveFile
                    },
                    contentDescription = null,
                    modifier = Modifier.size(15.dp),
                    tint = if (node.isDirectory) Color(0xFFFBBF24) else Color(0xFF94A3B8)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = node.name,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = if (node == selectedNode) MaterialTheme.colorScheme.primary else Color(0xFFE2E8F0)
                    )
                )
                if (node.size.isNotBlank()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = node.size,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            color = Color(0xFF64748B)
                        )
                    )
                }
            }

            if (node.isDirectory && isExpanded) {
                WorkspaceTreeView(
                    nodes = node.children,
                    selectedNode = selectedNode,
                    onSelect = onSelect,
                    depth = depth + 1
                )
            }
        }
    }
}
