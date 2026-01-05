package com.felix.ventral_android.data.repository

import android.net.Uri
import com.felix.ventral_android.data.dto.LoginRequestDto
import com.felix.ventral_android.data.dto.RegisterRequestDto
import com.felix.ventral_android.data.dto.UserDto
import com.felix.ventral_android.data.dto.toDomain
import com.felix.ventral_android.data.local.JwtUtils
import com.felix.ventral_android.data.local.LocalDataStore
import com.felix.ventral_android.data.network.api.UserApiService
import com.felix.ventral_android.data.network.cloudinary.CloudinaryManager
import com.felix.ventral_android.domain.model.User
import com.felix.ventral_android.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: UserApiService,
    override val localDataStore: LocalDataStore,
    private val cloudinaryManager: CloudinaryManager,
) : BaseRepository(), UserRepository {

    // Auth
    override suspend fun login(email: String, password: String): Result<Unit> {
        return handleApiCall(
            call = { apiService.loginUser(LoginRequestDto(email, password)) },
            map = { token ->
                // Extract data from the JWT payload
                val userId = JwtUtils.getUserIdFromToken(token)

                // Save to DataStore
                localDataStore.saveToken(token)
                if (userId != null) {
                    localDataStore.saveUserId(userId)
                }
                Unit
            }
        )
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String,
        phone: String,
        bio: String,
        imgUri: String,
        birth_date: String
    ): Result<Unit> {

        // 1. Handle the Image Upload FIRST (outside handleApiCall)
        val finalImageUrl = try {
            if (!imgUri.isNullOrEmpty()) {
                val uri = Uri.parse(imgUri)
                cloudinaryManager.uploadImage(uri) // Suspending call
            } else {
                "https://res.cloudinary.com/demo/image/upload/v1312461204/sample.jpg"
            }
        } catch (e: Exception) {
            return Result.failure(Exception("Image upload failed: ${e.message}"))
        }

        // 2. Prepare the DTO
        val request = RegisterRequestDto(
            name = name,
            email = email,
            password = password,
            phone = phone,
            bio = bio,
            img_url = finalImageUrl,
            date_of_birth = birth_date
        )

        // 3. Use handleApiCall for the network request and saving data
        return handleApiCall(
            call = { apiService.registerUser(request) },
            map = { token ->
                // Extract data from the JWT payload
                val userId = JwtUtils.getUserIdFromToken(token)

                // Save to DataStore
                localDataStore.saveToken(token)
                if (userId != null) {
                    localDataStore.saveUserId(userId)
                }
                Unit
            }
        )
    }


    // User Profile
    override suspend fun getUserById(userId: String): Result<User> {
        return handleApiCall(
            call = { apiService.getUserById(userId) },
            map = { dto -> dto.toDomain() }
        )
    }

}