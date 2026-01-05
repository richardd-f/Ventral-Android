package com.felix.ventral_android.data.dto

import com.felix.ventral_android.domain.model.User
import com.google.gson.annotations.SerializedName
import java.util.Date

data class UserDto(
    @SerializedName("user_id") val userId: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("bio") val bio: String,
    @SerializedName("img_url") val imgUrl: String,
    @SerializedName("date_of_birth") val dateOfBirth: String,
    @SerializedName("isAdmin") val isAdmin: Boolean,
    @SerializedName("_count") val count: UserCountDto
)

data class UserCountDto(
    @SerializedName("followers") val followers: Int,
    @SerializedName("following") val following: Int,
    @SerializedName("events") val events: Int
)

fun UserDto.toDomain(): User {
    return User(
        id = this.userId,
        name = this.name,
        email = this.email,
        phone = this.phone,
        bio = this.bio,
        imgUrl = this.imgUrl,
        followersCount = this.count.followers,
        followingCount = this.count.following,
        eventsCount = this.count.events
    )
}