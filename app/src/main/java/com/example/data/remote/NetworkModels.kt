package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val code: Int?, val message: String, val isOffline: Boolean = false) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
}

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String
)

@JsonClass(generateAdapter = true)
data class AuthResponse(
    @Json(name = "success") val success: Boolean = true,
    @Json(name = "token") val token: String? = null,
    @Json(name = "refreshToken") val refreshToken: String? = null,
    @Json(name = "user") val user: UserProfileDto? = null,
    @Json(name = "message") val message: String? = null
)

@JsonClass(generateAdapter = true)
data class UserProfileDto(
    @Json(name = "id") val id: String = "usr_vibe_99",
    @Json(name = "email") val email: String,
    @Json(name = "name") val name: String? = "Vibe Creator",
    @Json(name = "tier") val tier: String? = "Pro Vibe Coder"
)

@JsonClass(generateAdapter = true)
data class CloudProjectSyncRequest(
    @Json(name = "projects") val projects: List<CloudProjectDto>
)

@JsonClass(generateAdapter = true)
data class CloudProjectDto(
    @Json(name = "localId") val localId: Long,
    @Json(name = "title") val title: String,
    @Json(name = "mood") val mood: String,
    @Json(name = "behaviorPrompt") val behaviorPrompt: String,
    @Json(name = "generatedCode") val generatedCode: String,
    @Json(name = "updatedAt") val updatedAt: Long
)

@JsonClass(generateAdapter = true)
data class CloudSyncResponse(
    @Json(name = "syncedCount") val syncedCount: Int = 0,
    @Json(name = "status") val status: String = "SUCCESS",
    @Json(name = "syncedTimestamp") val syncedTimestamp: Long = System.currentTimeMillis()
)

data class EndpointHealthStatus(
    val authEndpoint: String = "https://auth.usafe.in",
    val apiEndpoint: String = "https://api.usafe.in",
    val isAuthReachable: Boolean = true,
    val isApiReachable: Boolean = true,
    val latencyMs: Long = 42,
    val lastChecked: Long = System.currentTimeMillis()
)
