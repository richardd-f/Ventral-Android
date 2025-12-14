package com.felix.ventral_android.data.dto

import com.google.gson.annotations.SerializedName

data class UserDto (
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("bio") val bio: String,
    @SerializedName("img_url") val img_url: String,
    @SerializedName("date_of_birth") val date_of_birth: String,
    @SerializedName("current_education") val current_education: String,
    @SerializedName("isAdmin") val isAdmin: Boolean,
)