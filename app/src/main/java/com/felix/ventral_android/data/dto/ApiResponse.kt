package com.felix.ventral_android.data.dto

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T?, // Make data nullable for error cases
    @SerializedName("errors") val errors: String? = null // New field for specific error details
)