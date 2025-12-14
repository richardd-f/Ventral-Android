package com.felix.ventral_android.domain.repository

import com.felix.ventral_android.data.dto.UserDto
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface UserRepository {
    fun login(email: String, password: String): Flow<Result<Unit>>
    fun register(name: String, email: String, password: String, phone: String, bio: String, img_url: String, birth_date: String): Flow<Result<Unit>>
    fun getUserProfile(): Flow<Result<UserDto>>
}