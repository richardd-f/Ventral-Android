package com.felix.ventral_android.data.network.api

import com.felix.ventral_android.data.dto.ApiResponse
import com.felix.ventral_android.data.dto.AuthResponseDto
import com.felix.ventral_android.data.dto.LoginRequestDto
import com.felix.ventral_android.data.dto.RegisterRequestDto
import com.felix.ventral_android.data.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApiService {

    // Login returns ApiResponse with AuthResponseDto inside the 'data' field
    @POST("login")
    suspend fun loginUser(
        @Body request: LoginRequestDto
    ): Response<ApiResponse<String>>

    // Register returns the same structure
    @POST("register")
    suspend fun registerUser(
        @Body request: RegisterRequestDto
    ): Response<ApiResponse<String>>

    // Profile endpoint
    @GET("users/id/{userId}")
    suspend fun getUserById(
        @Path("userId") userId: String
    ): Response<ApiResponse<UserDto>>
}