package com.example.data.remote

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object NetworkClient {

    private const val AUTH_BASE_URL = "https://auth.usafe.in"
    private const val API_BASE_URL = "https://api.usafe.in"

    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val securityHeaderInterceptor = Interceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("X-Client-Platform", "Android-VibeCoder")
            .header("X-Client-Version", "1.0.0")
        chain.proceed(requestBuilder.build())
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .addInterceptor(securityHeaderInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    val authService: USafeAuthService by lazy {
        Retrofit.Builder()
            .baseUrl(AUTH_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(USafeAuthService::class.java)
    }

    val apiService: USafeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(USafeApiService::class.java)
    }

    /**
     * Executes network calls with robust error handling and fallback recovery.
     */
    suspend fun <T> safeApiCall(call: suspend () -> retrofit2.Response<T>): NetworkResult<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    NetworkResult.Success(body)
                } else {
                    NetworkResult.Error(response.code(), "Empty response from server")
                }
            } else {
                val errorMsg = when (response.code()) {
                    401 -> "Authentication failed: Invalid credentials or expired session."
                    403 -> "Access forbidden: Insufficient permissions for auth.usafe.in."
                    404 -> "Requested endpoint not found."
                    500, 502, 503 -> "uSafe service temporarily unavailable. Please retry shortly."
                    else -> "Network error (${response.code()}): ${response.message()}"
                }
                NetworkResult.Error(response.code(), errorMsg)
            }
        } catch (e: IOException) {
            Log.w("NetworkClient", "Network IO exception", e)
            NetworkResult.Error(null, "Connection failed: Please check internet connectivity.", isOffline = true)
        } catch (e: Exception) {
            Log.e("NetworkClient", "Unexpected exception", e)
            NetworkResult.Error(null, "An unexpected error occurred: ${e.localizedMessage ?: "Unknown"}")
        }
    }
}
