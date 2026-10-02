package com.example.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface USafeAuthService {

    @POST("/api/v1/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @POST("/api/v1/auth/register")
    suspend fun register(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @GET("/api/v1/auth/verify")
    suspend fun verifyToken(
        @Header("Authorization") token: String
    ): Response<AuthResponse>
}
