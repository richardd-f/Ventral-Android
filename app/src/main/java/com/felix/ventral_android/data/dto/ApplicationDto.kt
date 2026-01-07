package com.felix.ventral_android.data.dto

import com.google.gson.annotations.SerializedName

data class ApplicationDto(
    @SerializedName("application_id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("event_id") val eventId: String,
    @SerializedName("timestamp") val timestamp: String,
)