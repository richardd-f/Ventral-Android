package com.felix.ventral_android.data.repository

import android.net.Uri
import com.felix.ventral_android.data.dto.LoginRequestDto
import com.felix.ventral_android.data.dto.RegisterRequestDto
import com.felix.ventral_android.data.dto.UserDto
import com.felix.ventral_android.data.local.LocalDataStore
import com.felix.ventral_android.data.network.api.UserApiService
import com.felix.ventral_android.data.network.cloudinary.CloudinaryManager
import com.felix.ventral_android.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: UserApiService,
    private val localDataSource: LocalDataStore,
    private val cloudinaryManager: CloudinaryManager
) : UserRepository {

    override fun login(email: String, password: String): Flow<Result<Unit>> = flow {
        try {
            val request = LoginRequestDto(email, password)
            val response = apiService.loginUser(request)

            if (response.isSuccessful && response.body() != null) {
                // SUCCESS (HTTP 200-299)
                // Retrofit automatically parsed the JSON for us
                val authData = response.body()!!
                localDataSource.saveToken(authData.token)
                emit(Result.success(Unit))
            } else {
                // ERROR (HTTP 400, 401, 500, etc.)
                val errorJsonString = response.errorBody()?.string() // Get raw JSON string

                // simple parsing to get the message
                val errorMessage = try {
                    // Assuming you use Gson or standard JSONObject
                    JSONObject(errorJsonString).getString("errors")
                } catch (e: Exception) {
                    "Unknown error occurred"
                }

                emit(Result.failure(Exception(errorMessage)))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun register(name: String, email: String, password: String, phone: String, bio: String, imgUri: String, birth_date: String) : Flow<Result<Unit>> = flow{
        try {

            // 1. Check if we have an image to upload
            val finalImageUrl = if (!imgUri.isNullOrEmpty()) {
                // Parse string back to Uri
                val uri = Uri.parse(imgUri)
                // UPLOAD HAPPENS HERE (Suspending call)
                cloudinaryManager.uploadImage(uri)
            } else {
                // Fallback default image or empty string
                "https://undefinedImage.com"
            }

            // Create the DTO object
            val request = RegisterRequestDto(
                name = name,
                email = email,
                password = password,
                phone = phone,
                bio = bio,
                img_url = finalImageUrl,
                date_of_birth = birth_date
            )

            // Call the API
            val response = apiService.registerUser(request)

            if (response.isSuccessful && response.body() != null) {
                val token = response.body()!!.token

                // Save token immediately so user is logged in
                localDataSource.saveToken(token)
                emit(Result.success(Unit))
            } else {
                val errorJsonString = response.errorBody()?.string() // Get raw JSON string
                val errorMessage = try {
                    JSONObject(errorJsonString).getString("errors")
                } catch (e: Exception) {
                    "Unknown error occurred"
                }
                emit(Result.failure(Exception(errorMessage)))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun getUserProfile(): Flow<Result<UserDto>> {
        TODO("Not yet implemented")
    }
}