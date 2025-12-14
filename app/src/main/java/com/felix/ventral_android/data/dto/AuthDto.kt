package com.felix.ventral_android.data.dto

import com.google.gson.annotations.SerializedName
import java.util.Date

data class LoginRequestDto (
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
)

data class RegisterRequestDto(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("bio") val bio: String,
    @SerializedName("img_url") val img_url: String,
    @SerializedName("date_of_birth") val date_of_birth: String
)

data class AuthResponseDto(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val token: String,
)
