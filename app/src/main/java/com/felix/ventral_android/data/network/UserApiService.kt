package com.felix.ventral_android.data.network

import com.felix.ventral_android.data.dto.AuthResponseDto
import com.felix.ventral_android.data.dto.LoginRequestDto
import com.felix.ventral_android.data.dto.RegisterRequestDto
import com.felix.ventral_android.data.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApiService{
    // Login endpoint
    @POST("login")
    suspend fun loginUser(
        @Body request: LoginRequestDto
    ): Response<AuthResponseDto>

    // Register endpoint
    @POST("register")
    suspend fun registerUser(
        @Body request: RegisterRequestDto
    ): Response<AuthResponseDto>

    // Profile endpoint (Requires the token usually)
    @GET("users/profile")
    suspend fun getUserProfile(): Response<UserDto>
}