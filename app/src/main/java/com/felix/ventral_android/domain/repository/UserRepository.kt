package com.felix.ventral_android.domain.repository

import com.felix.ventral_android.domain.model.User

interface UserRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(name: String, email: String, password: String, phone: String, bio: String, imgUri: String, birth_date: String): Result<Unit>
    suspend fun getUserById(userId: String): Result<User>
}