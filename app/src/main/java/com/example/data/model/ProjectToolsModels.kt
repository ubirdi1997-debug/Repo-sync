package com.example.data.model

enum class AgentPermissionMode(
    val title: String,
    val description: String,
    val iconEmoji: String
) {
    AUTO_PILOT(
        title = "Auto-Pilot",
        description = "Forge AI autonomously executes Git commits, SSH commands, and CLI scripts without pausing.",
        iconEmoji = "🤖"
    ),
    INTERACTIVE(
        title = "Interactive",
        description = "Forge AI pauses and prompts for confirmation before executing external DevOps commands.",
        iconEmoji = "🛡️"
    )
}

enum class WorkspaceViewMode(val title: String) {
    PREVIEW("UI Preview"),
    CODE("Source Code"),
    DIRECTORY("Files Tree")
}

data class GitConfig(
    val repoUrl: String = "https://github.com/developer/forge-vibe-project.git",
    val branch: String = "main",
    val autoCommit: Boolean = true,
    val personalAccessToken: String = "ghp_secure_usafe_token",
    val lastCommitHash: String? = "7f2a89c",
    val lastCommitMessage: String? = "feat: initial vibe state machine & reactive flow",
    val lastPushStatus: String = "Up to date with origin/main"
)

enum class SshAuthType(val title: String) {
    OPENSSH_KEY("OpenSSH Key (.pem / id_rsa)"),
    PASSWORD("ID & Password")
}

enum class SshStatus(val title: String, val colorHex: Long) {
    CONNECTED("Connected", 0xFF10B981),
    CONNECTING("Connecting...", 0xFF38BDF8),
    DISCONNECTED("Disconnected", 0xFF94A3B8),
    ERROR("Connection Failed", 0xFFEF4444)
}

data class SshServerConfig(
    val id: String,
    val name: String,
    val host: String,
    val port: Int = 22,
    val username: String,
    val authType: SshAuthType = SshAuthType.OPENSSH_KEY,
    val password: String = "",
    val privateKey: String = "",
    val passphrase: String = "",
    val status: SshStatus = SshStatus.DISCONNECTED,
    val lastPingMs: Long? = null
)

data class WorkspaceNode(
    val name: String,
    val path: String,
    val isDirectory: Boolean,
    val size: String = "",
    val children: List<WorkspaceNode> = emptyList(),
    val previewContent: String? = null
)

data class ToolInvocation(
    val toolName: String,
    val description: String,
    val command: String,
    val status: String = "EXECUTED", // PENDING, EXECUTED, CONFIRMED
    val output: String? = null
)

data class AiChatMessage(
    val id: String,
    val sender: String, // "user" | "forge_ai"
    val content: String,
    val toolInvocation: ToolInvocation? = null,
    val timestamp: Long = System.currentTimeMillis()
)
