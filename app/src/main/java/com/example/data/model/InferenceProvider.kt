package com.example.data.model

enum class InferenceProvider(
    val id: String,
    val displayName: String,
    val company: String,
    val defaultModel: String,
    val availableModels: List<String>,
    val defaultEndpoint: String,
    val description: String,
    val isDefaultConfigured: Boolean = false
) {
    GEMINI(
        id = "gemini",
        displayName = "Gemini AI",
        company = "Google",
        defaultModel = "gemini-2.5-flash",
        availableModels = listOf("gemini-2.5-flash", "gemini-2.5-pro", "gemini-1.5-flash-8b"),
        defaultEndpoint = "https://generativelanguage.googleapis.com",
        description = "High-speed reasoning, 1M+ token context window, optimal for vibe synthesis.",
        isDefaultConfigured = true
    ),
    BEDROCK(
        id = "bedrock",
        displayName = "Amazon Bedrock",
        company = "AWS",
        defaultModel = "anthropic.claude-3-5-sonnet",
        availableModels = listOf("anthropic.claude-3-5-sonnet", "meta.llama3-70b-instruct", "amazon.titan-text-premier"),
        defaultEndpoint = "https://bedrock-runtime.us-east-1.amazonaws.com",
        description = "Enterprise AI foundation models through unified AWS IAM credentials."
    ),
    DIGITAL_OCEAN(
        id = "digital_ocean",
        displayName = "DigitalOcean GenAI",
        company = "DigitalOcean",
        defaultModel = "meta-llama-3.1-70b-instruct",
        availableModels = listOf("meta-llama-3.1-70b-instruct", "mistralai/mistral-nemo", "meta-llama-3.1-8b-instruct"),
        defaultEndpoint = "https://inference.cloud.digitalocean.com/v1",
        description = "Developer-friendly cloud inference with guaranteed regional data privacy."
    ),
    VERTEX_AI(
        id = "vertex_ai",
        displayName = "Google Cloud Vertex AI",
        company = "Google Cloud",
        defaultModel = "gemini-1.5-pro-vertex",
        availableModels = listOf("gemini-1.5-pro-vertex", "claude-3-5-sonnet-vertex", "codegeex-4"),
        defaultEndpoint = "https://us-central1-aiplatform.googleapis.com/v1",
        description = "Production ML pipelines, VPC security, and low-latency API serving."
    ),
    OPENCLAW(
        id = "openclaw",
        displayName = "uSafe OpenClaw Proxy",
        company = "uSafe.in",
        defaultModel = "openclaw-auto-router-v2",
        availableModels = listOf("openclaw-auto-router-v2", "openclaw-deep-reasoner", "openclaw-low-latency"),
        defaultEndpoint = "https://api.usafe.in/v1/openclaw",
        description = "Unified open routing proxy by uSafe with automatic rate-limit failover.",
        isDefaultConfigured = true
    )
}

data class ProviderKeyConfig(
    val providerId: String,
    val apiKey: String,
    val selectedModel: String,
    val customEndpoint: String? = null,
    val temperature: Float = 0.7f,
    val maxOutputTokens: Int = 4096,
    val isEnabled: Boolean = true
)
