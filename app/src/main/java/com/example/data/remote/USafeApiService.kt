package com.example.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface USafeApiService {

    @POST("/api/v1/projects/sync")
    suspend fun syncProjects(
        @Header("Authorization") token: String,
        @Body request: CloudProjectSyncRequest
    ): Response<CloudSyncResponse>

    @GET("/api/v1/projects")
    suspend fun fetchCloudProjects(
        @Header("Authorization") token: String
    ): Response<List<CloudProjectDto>>

    @GET("/api/v1/health")
    suspend fun checkHealth(): Response<Map<String, String>>
}
