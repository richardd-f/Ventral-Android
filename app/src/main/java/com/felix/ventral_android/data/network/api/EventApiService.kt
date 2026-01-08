package com.felix.ventral_android.data.network.api

import com.felix.ventral_android.data.dto.ApiResponse
import com.felix.ventral_android.data.dto.ApplicationDto
import com.felix.ventral_android.data.dto.CategoryDto
import com.felix.ventral_android.data.dto.CreateEventRequestDto
import com.felix.ventral_android.data.dto.EventDto
import com.felix.ventral_android.data.dto.UpdateEventRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface EventApiService {

    @GET("events/user/{userId}")
    suspend fun getEventsByUserId(
        @Path("userId") userId: String
    ): Response<ApiResponse<List<EventDto>>> // List nested in ApiResponse

    @GET("events")
    suspend fun getAllEvents(): Response<ApiResponse<List<EventDto>>> // List nested in ApiResponse

    @POST("events")
    suspend fun createEvent(
        @Body eventRequest: CreateEventRequestDto
    ): Response<ApiResponse<EventDto>> // Single object nested

    @PATCH("events/{eventId}")
    suspend fun updateEvent(
        @Path("eventId") eventId: String,
        @Body updateRequest: UpdateEventRequestDto
    ): Response<ApiResponse<EventDto>> // Single object nested

    @DELETE("events/{eventId}")
    suspend fun deleteEvent(
        @Path("eventId") eventId: String
    ): Response<ApiResponse<String>> // Only success/message needed


    @GET("events/categories")
    suspend fun getAllCategories(): Response<ApiResponse<List<CategoryDto>>> // List nested in ApiResponse

    // Apply logged user to event
    @POST("events/{eventId}/apply")
    suspend fun applyEvent(
        @Path("eventId") eventId: String
    ): Response<ApiResponse<ApplicationDto>>


}