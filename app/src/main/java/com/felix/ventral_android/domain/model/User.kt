package com.felix.ventral_android.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val bio: String,
    val imgUrl: String,
    val followersCount: Int,
    val followingCount: Int,
    val eventsCount: Int
)