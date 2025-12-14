package com.felix.ventral_android.domain.repository

import com.felix.ventral_android.data.dto.UserDto
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun login(email: String, password: String): Flow<Result<Unit>> // Return Unit or Boolean if success
    fun getUserProfile(): Flow<Result<UserDto>>
}